package com.github.rossdanderson.stateless4k.config

import com.github.rossdanderson.stateless4k.model.TriggerReferenceRepresentation
import java.util.*

class SingleTriggerReferenceConfiguration<S, T : Any, V>(private val stateConfiguration: StateLevelConfiguration<S, T, V>,
                                                         private val sourceState: S,
                                                         private val trigger: T) :
        SingleTriggerConfiguration<S, T, V>,
        StateConfiguration<S, T, V> by stateConfiguration {

    private val transitionConfigurations = ArrayList<SingleTransitionConfiguration<S, T, V>>()

    override fun transitionTo(destination: S,
                              init: (TransitionConfiguration<S, T, V>.() -> Unit)): TransitionConfiguration<S, T, V> {

        if (destination == sourceState) {
            throw IllegalStateException("Not allowed to transition to same state <$sourceState>. Use reenter or doNothing.")
        }

        val transitionConfiguration = SingleTransitionConfiguration(stateConfiguration, destination).apply(init)
        transitionConfigurations.add(transitionConfiguration)
        return transitionConfiguration
    }

    override fun doNothing(init: (TransitionConfiguration<S, T, V>.() -> Unit)): TransitionConfiguration<S, T, V> {
        val transitionConfiguration = SingleTransitionConfiguration(stateConfiguration, null).apply(init)
        transitionConfigurations.add(transitionConfiguration)
        return transitionConfiguration
    }

    override fun reenter(init: (TransitionConfiguration<S, T, V>.() -> Unit)): TransitionConfiguration<S, T, V> {
        val transitionConfiguration = SingleTransitionConfiguration(stateConfiguration, sourceState).apply(init)
        transitionConfigurations.add(transitionConfiguration)
        return transitionConfiguration
    }

    override fun build() =
            TriggerReferenceRepresentation(sourceState, trigger, transitionConfigurations.map { it.build() })
}