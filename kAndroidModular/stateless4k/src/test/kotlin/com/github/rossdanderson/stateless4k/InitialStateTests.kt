package com.github.rossdanderson.stateless4k

import com.github.rossdanderson.stateless4k.config.StateMachineConfiguration
import com.github.rossdanderson.stateless4k.interfaces.StateAction
import com.github.rossdanderson.stateless4k.model.StateMachineModel
import org.junit.Assert.*
import org.junit.Test

class InitialStateTests {

    private var executed = false

    @Test
    fun testInitialStateEntryActionNotExecuted() {
        val initial = TestState.B

        val model = config(initial)

        val sm = model.start(TestTarget(), initial, false)
        assertEquals(initial, sm.state)
        assertFalse(executed)
    }

    @Test
    fun testInitialStateEntryActionExecuted() {
        val initial = TestState.B

        val model = config(initial)

        val sm = model.start(TestTarget(), initial, true)

        assertEquals(initial, sm.state)
        assertTrue(executed)
    }

    private fun config(initial: TestState): StateMachineModel<TestState, TestTrigger, TestTarget> {
        val config = StateMachineConfiguration.create<TestState, TestTrigger, TestTarget>()
        config.state(initial)
                .onEntry(object : StateAction<TestState, TestTrigger, TestTarget> {
                    override fun invoke(target: TestTarget, transition: Transition<TestState, TestTrigger>) {
                        executed = true
                    }
                })
        return config.build()
    }
}
