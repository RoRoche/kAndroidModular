package com.github.rossdanderson.stateless4k.model

import com.github.rossdanderson.stateless4k.StateMachine
import com.github.rossdanderson.stateless4k.Transition

class StateMachineModel<S, T : Any, V>(private val allStateRepresentations: Map<S, StateRepresentation<S, T, V>>) {

    fun start(target: V,
              initialState: S,
              performEntryActionsForInitialState: Boolean = false): StateMachine<S, T, V> {
        var state: S = initialState
        return start(target, initialState, { state }, { state = it }, performEntryActionsForInitialState)
    }

    fun start(target: V,
              initialState: S,
              stateAccessor: () -> S,
              stateMutator: (S) -> Unit,
              performEntryActionsForInitialState: Boolean = false): StateMachine<S, T, V> {

        val stateManager = StateMachine(target, allStateRepresentations, stateAccessor, stateMutator)

        stateMutator(initialState)

        if (performEntryActionsForInitialState) allStateRepresentations[initialState]!!.enter(target, Transition<S, T>(null, initialState, null))

        return stateManager
    }
}