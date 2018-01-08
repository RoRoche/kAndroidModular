package com.github.rossdanderson.stateless4k.config

import com.github.rossdanderson.stateless4k.model.TransitionRepresentation
import java.util.*

class SingleTransitionConfiguration<S, T : Any, V>(private val stateConfiguration: StateConfiguration<S, T, V>,
                                                   private val destination: S?) :
        TransitionConfiguration<S, T, V>,
        StateConfiguration<S, T, V> by stateConfiguration {

    val guards = ArrayList<(T, V) -> Boolean>()

    override fun assuming(guard: (T, V) -> Boolean): StateConfiguration<S, T, V> {
        guards.add(guard)
        return stateConfiguration
    }

    fun build() = TransitionRepresentation(destination, guards)
}