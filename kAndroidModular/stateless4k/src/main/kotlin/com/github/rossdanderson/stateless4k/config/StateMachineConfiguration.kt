package com.github.rossdanderson.stateless4k.config

import com.github.rossdanderson.stateless4k.model.StateMachineModel
import com.github.rossdanderson.stateless4k.model.StateRepresentation
import java.util.*

/**
 * The state machine configuration
 */
class StateMachineConfiguration<S, T : Any, V> private constructor() : SuperstateConfiguration<S, T, V> {

    private val stateConfigurationFactory = StateConfigurationFactory<S, T, V>()

    private val substateConfigurations = HashMap<S, StateLevelConfiguration<S, T, V>>()

    companion object {
        fun <S, T : Any, V> create(): StateMachineConfiguration<S, T, V> =
                create { }

        fun <S, T : Any, V> create(init: StateMachineConfiguration<S, T, V>.() -> Unit) =
                StateMachineConfiguration<S, T, V>().apply(init)
    }

    override fun state(state: S,
                       init: StateConfiguration<S, T, V>.() -> Unit): StateConfiguration<S, T, V> =
            substateConfigurations.computeIfAbsent(state) {
                stateConfigurationFactory.createConfiguration(state).apply(init)
            }

    fun build(): StateMachineModel<S, T, V> =
            StateMachineModel(substateConfigurations
                    .map { it.value.build(null) }
                    .flatMap(StateRepresentation<S, T, V>::flatten)
                    .toMap())
}
