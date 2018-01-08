package com.github.rossdanderson.stateless4k.model

data class TriggerReferenceRepresentation<out S, T, in V>(val sourceState: S,
                                                          val trigger: T,
                                                          override val transitionRepresentations: List<TransitionRepresentation<S, T, V>>)
    : TriggerRepresentation<S, T, V> {

    override fun matchesTrigger(trigger: T) =
            this.trigger == trigger

    override fun isPermitted(trigger: T, target: V) =
            transitionRepresentations.filter { it.isPermitted(trigger, target) }.isNotEmpty()
}