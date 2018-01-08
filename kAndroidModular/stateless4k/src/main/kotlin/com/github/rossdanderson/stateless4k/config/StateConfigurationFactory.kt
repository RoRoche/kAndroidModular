package com.github.rossdanderson.stateless4k.config

import java.util.*

class StateConfigurationFactory<S, T : Any, V> {

    private val stateConfigurations = HashMap<S, StateLevelConfiguration<S, T, V>>()

    fun createConfiguration(state: S, superstateConfiguration: StateLevelConfiguration<S, T, V>? = null): StateLevelConfiguration<S, T, V> {

        val existingStateConfiguration = stateConfigurations[state]
        val superstate = existingStateConfiguration?.superstateConfiguration?.state
        if (existingStateConfiguration != null) {
            if (superstateConfiguration?.state != superstate) {
                throw IllegalStateException("State $state has already been defined as a " +
                        if (superstate == null) "top level state" else "substate of $superstate")
            }
            return existingStateConfiguration
        }

        val newStateConfiguration = StateLevelConfiguration(superstateConfiguration, state, this)
        stateConfigurations.put(state, newStateConfiguration)
        return newStateConfiguration
    }
}