package com.github.rossdanderson.stateless4k.model

import com.github.rossdanderson.stateless4k.Transition
import com.github.rossdanderson.stateless4k.config.StateLevelConfiguration
import com.github.rossdanderson.stateless4k.interfaces.StateAction

class StateRepresentation<S, T : Any, V> internal constructor(val state: S,
                                                              val superstateRepresentation: StateRepresentation<S, T, V>? = null,
                                                              val triggerRepresentations: List<TriggerRepresentation<S, T, V>>,
                                                              val entryActions: List<StateAction<S, T, V>>,
                                                              val exitActions: List<StateAction<S, T, V>>,
                                                              substateConfigurations: Map<S, StateLevelConfiguration<S, T, V>>) {

    val substateRepresentations = substateConfigurations.map {
        it.value.build(this)
    }

    internal fun canHandle(trigger: T, target: V) =
            getTransition(trigger, target) != null

    internal fun getTransition(trigger: T, target: V): TransitionRepresentation<S, T, V>? =
            getLocalTransition(trigger, target) ?: superstateRepresentation?.getTransition(trigger, target)

    internal fun getLocalTransition(trigger: T, target: V): TransitionRepresentation<S, T, V>? {

        val transitionRepresentations = triggerRepresentations.firstOrNull { it.matchesTrigger(trigger) }?.transitionRepresentations ?: return null

        val permittedTransitionRepresentations = transitionRepresentations.filter { it.isPermitted(trigger, target) }

        if (permittedTransitionRepresentations.size > 1)
            throw IllegalStateException("Multiple permitted exit transitions are configured from state '$state' " +
                    "for trigger '$trigger'. Guard clauses must be mutually exclusive.")

        return permittedTransitionRepresentations.single()
    }

    internal fun enter(target: V,
                       transition: Transition<S, T>) {

        fun executeEntryActions(target: V,
                                transition: Transition<S, T>) = entryActions.forEach { it(target, transition) }

        if (transition.isReentry) executeEntryActions(target, transition)
        else if (transition.source == null || !includes(transition.source)) {
            superstateRepresentation?.enter(target, transition)
            executeEntryActions(target, transition)
        }
    }

    /**
     * Checks whether the given [state] matches the current state representation, or any of its substate representations
     */
    internal fun includes(state: S): Boolean {
        return this.state == state || substateRepresentations.filter { it.includes(state) }.isNotEmpty()
    }

    /**
     * Checks whether the given [state] matches the current state representation, of any of its superstate representations
     */
    internal fun isIncludedIn(state: S): Boolean {
        return this.state == state || superstateRepresentation?.isIncludedIn(state) ?: false
    }

    internal fun flatten(): List<Pair<S, StateRepresentation<S, T, V>>> {
        return listOf(Pair(state, this)) + this.substateRepresentations.flatMap { it.flatten() }
    }
}
