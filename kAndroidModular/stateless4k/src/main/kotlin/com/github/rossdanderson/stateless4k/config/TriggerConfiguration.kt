package com.github.rossdanderson.stateless4k.config

interface TriggerConfiguration<S, T : Any, V> : StateConfiguration<S, T, V> {

    fun transitionTo(state: S): TransitionConfiguration<S, T, V> =
            transitionTo(state, { })

    fun transitionTo(destination: S,
                     init: (TransitionConfiguration<S, T, V>.() -> Unit)): TransitionConfiguration<S, T, V>

    fun doNothing(): TransitionConfiguration<S, T, V> =
            doNothing({ })

    fun doNothing(init: (TransitionConfiguration<S, T, V>.() -> Unit)): TransitionConfiguration<S, T, V>

    fun reenter(): TransitionConfiguration<S, T, V> =
            reenter({ })

    fun reenter(init: (TransitionConfiguration<S, T, V>.() -> Unit)): TransitionConfiguration<S, T, V>
}