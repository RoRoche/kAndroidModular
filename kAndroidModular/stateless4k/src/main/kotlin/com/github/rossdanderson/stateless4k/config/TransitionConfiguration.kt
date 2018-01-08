package com.github.rossdanderson.stateless4k.config

interface TransitionConfiguration<S, T : Any, V> : StateConfiguration<S, T, V> {

    fun assuming(guard: (T, V) -> Boolean): StateConfiguration<S, T, V>
}