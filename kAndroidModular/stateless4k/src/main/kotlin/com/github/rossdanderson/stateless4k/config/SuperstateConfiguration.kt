package com.github.rossdanderson.stateless4k.config

@Suppress("unused")
interface SuperstateConfiguration<S, T : Any, V> {
    /**
     * Begin configuration of the entry/exit actions and allowed transitions
     * when the state machine is in a particular state
     * @param state The state to configure
     * @return A configuration object through which the state can be configured
     */
    fun state(state: S): StateConfiguration<S, T, V> =
            state(state, { })

    /**
     * Begin configuration of the entry/exit actions and allowed transitions
     * when the state machine is in a particular state
     * @param state The state to configure
     * @return A configuration object through which the state can be configured
     */
    fun state(state: S,
              init: StateConfiguration<S, T, V>.() -> Unit): StateConfiguration<S, T, V>
}