package com.github.rossdanderson.stateless4k

import com.github.rossdanderson.stateless4k.config.StateMachineConfiguration
import org.junit.Assert.*
import org.junit.Test

class StateMachineTests {

    private val StateA = TestState.A
    private val StateB = TestState.B
    private val StateC = TestState.C
    private val TriggerX = TestTrigger.X
    private val TriggerY = TestTrigger.Y
    private var fired = false

    class Target

    @Test
    fun CanUseReferenceTypeMarkers() {
        RunSimpleTest(
                arrayOf<Enum<*>>(StateA, StateB, StateC),
                arrayOf<Enum<*>>(TriggerX, TriggerY), Target())
    }

    @Test
    fun CanUseValueTypeMarkers() {
        RunSimpleTest(TestState.values(), TestTrigger.values(), Target())
    }

    private fun <S, T : Any, V> RunSimpleTest(states: Array<S>, transitions: Array<T>, target: V) {
        val a = states[0]
        val b = states[1]
        val x = transitions[0]

        val config = StateMachineConfiguration.create<S, T, V> {
            state(a) {
                on(x) {
                    transitionTo(b)
                }
            }

            state(b)
        }

        val sm = config.build().start(target, a)
        sm.fire(x)

        assertEquals(b, sm.state)
    }

    @Test
    fun InitialStateIsCurrent() {
        val initial = TestState.B

        val sm = StateMachineConfiguration.create<TestState, TestTrigger, Target>().build().start(Target(), initial)
        assertEquals(initial, sm.state)
    }

    @Test
    fun SubstateIsIncludedInCurrentState() {
        val config = StateMachineConfiguration.create<TestState, TestTrigger, Target>()

        config.state(TestState.C).state(TestState.B)

//        config.state(State.B).stateOf(State.C)

        val sm = config.build().start(Target(), TestState.B)

        assertEquals(TestState.B, sm.state)
        assertTrue(sm.isInState(TestState.C))
    }

    @Test
    fun WhenInSubstate_TriggerIgnoredInSuperstate_RemainsInSubstate() {
        val config = StateMachineConfiguration.create<TestState, TestTrigger, Target>()


        config.state(TestState.C)
                .on(TriggerX).doNothing()
                .state(TestState.B)

        val sm = config.build().start(Target(), TestState.B)
        sm.fire(TestTrigger.X)

        assertEquals(TestState.B, sm.state)
    }

//    @Test
//    fun PermittedTriggersIncludeSuperstatePermittedTriggers() {
//        val config = StateMachineConfiguration.create<TestState, TestTrigger, Target>()
//
//        config.state(TestState.A).on(TestTrigger.Z).transitionTo(TestState.B)
//
//        config.state(TestState.C)
//                .on(TestTrigger.Y).transitionTo(TestState.A)
//                .state(TestState.B).on(TestTrigger.X).transitionTo(TestState.A)
//
//        val sm = config.build().start(Target(), TestState.B)
//        val permitted = sm.permittedTriggers
//
//        assertTrue(permitted.contains(TestTrigger.X))
//        assertTrue(permitted.contains(TestTrigger.Y))
//        assertFalse(permitted.contains(TestTrigger.Z))
//    }
//
//    @Test
//    fun PermittedTriggersAreDistinctValues() {
//        val config = StateMachineConfiguration.create<TestState, TestTrigger, Target>()
//
//        config.state(TestState.C)
//                .on(TestTrigger.X).transitionTo(TestState.B)
//                .state(TestState.B)
//                .on(TestTrigger.X).transitionTo(TestState.A)
//
//        val sm = config.build().start(Target(), TestState.B)
//        val permitted = sm.permittedTriggers
//
//        assertEquals(1, permitted.size.toLong())
//        assertEquals(TestTrigger.X, permitted[0])
//    }
//
//    @Test
//    fun AcceptedTriggersRespectGuards() {
//        val config = StateMachineConfiguration.create<TestState, TestTrigger, Target>()
//
//        config.state(TestState.B)
//                .on(TestTrigger.X).transitionTo(TestState.A).assuming { false }
//
//        val sm = config.build().start(Target(), TestState.B)
//
//        assertEquals(0, sm.permittedTriggers.size.toLong())
//    }

    @Test
    fun WhenDiscriminatedByGuard_ChoosesPermitedTransition() {
        val config = StateMachineConfiguration.create<TestState, TestTrigger, Target> {
            state(TestState.A)

            state(TestState.B)
                    .on(TestTrigger.X).transitionTo(TestState.A).assuming({ trigger, target -> false })
                    .on(TestTrigger.X).transitionTo(TestState.C).assuming({ trigger, target -> true })

            state(TestState.C)
        }

        val sm = config.build().start(Target(), TestState.B)
        sm.fire(TestTrigger.X)

        assertEquals(TestState.C, sm.state)
    }

    private fun setFired() {
        fired = true
    }

    @Test
    fun WhenTriggerIsIgnored_ActionsNotExecuted() {
        val config = StateMachineConfiguration.create<TestState, TestTrigger, Target>()

        config.state(TestState.B) {
            onEntry { target, transition -> setFired() }
            on(TriggerX) {
                doNothing()
            }
        }

        fired = false

        val sm = config.build().start(Target(), TestState.B)
        sm.fire(TestTrigger.X)

        assertFalse(fired)
    }

    @Test
    fun IfSelfTransitionPermited_ActionsFire() {
        val config = StateMachineConfiguration.create<TestState, TestTrigger, Target>()

        config.state(TestState.B) {
            onEntry({ target, transition -> setFired() })
            on(TriggerX) {
                reenter()
            }
        }

        fired = false

        val sm = config.build().start(Target(), TestState.B)
        sm.fire(TestTrigger.X)

        assertTrue(fired)
    }

    @Test(expected = IllegalStateException::class)
    fun ImplicitReentryIsDisallowed() {
        val config = StateMachineConfiguration.create<TestState, TestTrigger, Target>()

        val sm = config.build().start(Target(), TestState.B)

        config.state(TestState.B).on(TestTrigger.X).transitionTo(TestState.B)
    }

    //    @Test(expected = IllegalStateException.class)
    //    public void TriggerParametersAreImmutableOnceSet() {
    //        StateMachine<State, Trigger> config = new StateMachine<>();
    //
    //        StateManager<State, Trigger> sm = StateManager.Companion.create(State.B);
    //
    ////        config.setTriggerParameters(Trigger.X, String.class, Integer.class);
    //        config.setTriggerParameters(Trigger.X, String.class);
    //    }

    //        @Test
    //        public void ParametersSuppliedToFireArePassedToEntryAction()
    //        {
    //        	StateManager<State, Trigger> sm = new StateManager<State, Trigger>(State.B);
    //
    //        	TriggerWithParameters2<String, Integer, State, Trigger> x = sm.setTriggerParameters(Trigger.X, String.class, int.class);
    //
    //            sm.state(State.B)
    //                .permit(Trigger.X, State.C);
    //
    //
    //            sm.state(State.C)
    //                .onEntryFrom(x, new Action2<String, int>() {
    //                	public void doIt(String s, int i) {
    //                		entryArgS = s;
    //                        entryArgI = i;
    //				});
    //
    //            var suppliedArgS = "something";
    //            var suppliedArgI = 42;
    //
    //            sm.fire(x, suppliedArgS, suppliedArgI);
    //
    //            AreEqual(suppliedArgS, entryArgS);
    //            AreEqual(suppliedArgI, entryArgI);
    //        }
    //
    //        @Test
    //        public void WhenAnUnhandledTriggerIsFired_TheProvidedHandlerIsCalledWithStateAndTrigger()
    //        {
    //            var sm = new StateManager<State, Trigger>(State.B);
    //
    //            State? state = null;
    //            Trigger? trigger = null;
    //            sm.onUnhandledTrigger((s, t) =>
    //                                      {
    //                                          state = s;
    //                                          trigger = t;
    //                                      });
    //
    //            sm.fire(Trigger.Z);
    //
    //            AreEqual(State.B, state);
    //            AreEqual(Trigger.Z, trigger);
    //        }
}
