package com.github.rossdanderson.stateless4k.config

class GroupTriggerConfiguration<S, T : Any, V>(private val stateConfiguration: StateConfiguration<S, T, V>,
                                               private val triggerConfigurations: Collection<TriggerConfiguration<S, T, V>>) :
        TriggerConfiguration<S, T, V>,
        StateConfiguration<S, T, V> by stateConfiguration {

    override fun transitionTo(destination: S,
                              init: (TransitionConfiguration<S, T, V>.() -> Unit)): TransitionConfiguration<S, T, V> =
            GroupTransitionConfiguration(stateConfiguration, triggerConfigurations.map { it.transitionTo(destination) }).apply(init)

    override fun doNothing(init: (TransitionConfiguration<S, T, V>.() -> Unit)): TransitionConfiguration<S, T, V> =
            GroupTransitionConfiguration(stateConfiguration, triggerConfigurations.map { it.doNothing() }).apply(init)

    override fun reenter(init: (TransitionConfiguration<S, T, V>.() -> Unit)): TransitionConfiguration<S, T, V> =
            GroupTransitionConfiguration(stateConfiguration, triggerConfigurations.map { it.reenter() }).apply(init)
}