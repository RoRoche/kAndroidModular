package com.github.rossdanderson.stateless4k

/**
 * An object representing the transition causing the current action to be invoked.
 * [source] is the state the model is leaving, and [destination] is the state it is transitioning to.
 * [trigger] is the trigger type that caused the transition to begin.
 * [source] and [trigger] will be `null` if we are entering the initial state.
 */
data class Transition<out S, out T>(val source: S?,
                                    val destination: S,
                                    val trigger: T?) {

    /**
     * `true` if the state is transitioning back into itself
     */
    val isReentry: Boolean
        get() = source == destination
}
