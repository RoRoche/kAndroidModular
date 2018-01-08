package com.github.rossdanderson.stateless4k.model

interface TriggerRepresentation<out S, in T, in V> {

    val transitionRepresentations: List<TransitionRepresentation<S, T, V>>

    fun isPermitted(trigger: T, target: V): Boolean

    fun matchesTrigger(trigger: T): Boolean
}