package com.github.rossdanderson.stateless4k.model

data class TriggerTypeRepresentation<out S, T : Any, in V>(val sourceState: S,
                                                           val triggerType: Class<T>,
                                                           override val transitionRepresentations: List<TransitionRepresentation<S, T, V>>)
    : TriggerRepresentation<S, T, V> {

    override fun isPermitted(trigger: T, target: V) =
            transitionRepresentations.filter { it.isPermitted(trigger, target) }.isNotEmpty()

    override fun matchesTrigger(trigger: T): Boolean {
        return triggerType.isInstance(trigger)
    }
}