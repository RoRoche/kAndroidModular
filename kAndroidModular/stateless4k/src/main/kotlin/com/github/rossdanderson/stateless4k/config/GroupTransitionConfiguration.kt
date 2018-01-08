package com.github.rossdanderson.stateless4k.config

class GroupTransitionConfiguration<S, T : Any, V>(private val stateConfiguration: StateConfiguration<S, T, V>,
                                                  private val transitionConfigurations: Collection<TransitionConfiguration<S, T, V>>) :
        TransitionConfiguration<S, T, V>,
        StateConfiguration<S, T, V> by stateConfiguration {

    override fun assuming(guard: (T, V) -> Boolean): StateConfiguration<S, T, V> {
        transitionConfigurations.forEach { it.assuming(guard) }
        return stateConfiguration
    }
}