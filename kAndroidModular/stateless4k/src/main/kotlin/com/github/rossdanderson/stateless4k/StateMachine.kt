package com.github.rossdanderson.stateless4k

import com.github.rossdanderson.stateless4k.model.StateRepresentation
import org.slf4j.LoggerFactory

/**
 * Models behaviour as transitions between a finite set of states
 *
 * @param S The type used to represent the states
 * @param T The type used to represent the triggers that cause state transitions
 * @param V the type to apply entry/exit actions to.
 */
class StateMachine<S, T : Any, V>(private val target: V,
                                  private val allStateRepresentations: Map<S, StateRepresentation<S, T, V>>,
                                  private val stateAccessor: () -> S,
                                  private val stateMutator: (S) -> Unit) {

    private val logger = LoggerFactory.getLogger(StateMachine::class.java)

    private var unhandledTriggerAction: (S, T) -> Unit = { state, trigger ->
        throw IllegalStateException("$target - No valid leaving transitions are permitted from state <$state> for trigger <$trigger>. " +
                "Consider ignoring the trigger.")
    }

    /**
     * The current state
     * @return The current state
     */
    var state: S
        get() = stateAccessor.invoke()
        private set(value) = stateMutator.invoke(value)

    private val currentRepresentation: StateRepresentation<S, T, V>
        get() = allStateRepresentations[state] ?: throw IllegalStateException("$target - State <$state> is not valid - Please ensure it has been configured.")

    /**
     * Transition from the current state via the specified trigger.
     * The target state is determined by the configuration of the current state.
     * Actions associated with leaving the current state and entering the new one
     * will be invoked
     * @param trigger The trigger to fire
     */
    fun fire(trigger: T) {
        logger.info("$target - Firing <$trigger>")

        val transitionRepresentation = currentRepresentation.getTransition(trigger, target)
        if (transitionRepresentation == null) {
            unhandledTriggerAction.invoke(currentRepresentation.state, trigger)
            return
        }

        fun StateRepresentation<S, T, V>.exit(transition: Transition<S, T>) {
            fun executeExitActions(transition: Transition<S, T>) = exitActions.forEach { it.invoke(target, transition) }

            if (transition.isReentry) executeExitActions(transition)
            else if (!includes(transition.destination)) {
                executeExitActions(transition)
                superstateRepresentation?.exit(transition)
            }
        }

        val destination = transitionRepresentation.destination
        if (destination == null) {
            logger.debug("$target - Ignoring trigger <$trigger>")
        } else {
            logger.info("$target - <$trigger> trigger causing state transition <$state> -> <$destination>")
            val transition: Transition<S, T> = Transition(state, destination, trigger)
            currentRepresentation.exit(transition)
            state = destination
            currentRepresentation.enter(target, transition)
        }
    }

    /**
     * Override the default behaviour of throwing an exception when an unhandled trigger is fired
     * @param unhandledTriggerAction An action to call when an unhandled trigger is fired
     */
    fun onUnhandledTrigger(unhandledTriggerAction: (S, T) -> Unit) {
        this.unhandledTriggerAction = unhandledTriggerAction
    }

    /**
     * Determine if the state machine is in the supplied state
     * @param state The state to test for
     * @return True if the current state is equal to, or a state of, the supplied state
     */
    fun isInState(state: S) =
            currentRepresentation.isIncludedIn(state)

    /**
     * Returns true if `trigger` can be fired  in the current state
     * @param trigger Trigger to test
     * @return True if the trigger can be fired, false otherwise
     */
    fun canFire(trigger: T) =
            currentRepresentation.canHandle(trigger, target)
}
