package com.github.rossdanderson.stateless4k.config

import com.github.rossdanderson.stateless4k.model.TriggerRepresentation

interface SingleTriggerConfiguration<S, T : Any, V> : TriggerConfiguration<S, T, V> {

    fun build(): TriggerRepresentation<S, T, V>
}