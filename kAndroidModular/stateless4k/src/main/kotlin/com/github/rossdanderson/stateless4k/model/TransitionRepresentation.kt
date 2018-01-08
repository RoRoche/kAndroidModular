package com.github.rossdanderson.stateless4k.model

data class TransitionRepresentation<out S, in T, in V>(val destination: S?,
                                                       val guards: List<(T, V) -> Boolean>) {

    fun passingGuards(trigger: T, target: V): List<(T, V) -> Boolean> =
            guards.filter { it(trigger, target) }

    fun isPermitted(trigger: T, target: V) =
            guards.isEmpty() || passingGuards(trigger, target).isNotEmpty()
}