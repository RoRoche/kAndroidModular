package com.github.rossdanderson.stateless4k.config

import com.github.rossdanderson.stateless4k.Transition
import com.github.rossdanderson.stateless4k.interfaces.StateAction
import com.github.rossdanderson.stateless4k.model.StateRepresentation
import java.util.*

@Suppress("unused")
class StateLevelConfiguration<S, T : Any, V>(internal val superstateConfiguration: StateLevelConfiguration<S, T, V>? = null,
                                             internal val state: S,
                                             private val stateConfigurationFactory: StateConfigurationFactory<S, T, V>)
    : StateConfiguration<S, T, V> {

    private val triggerConfigurations = HashMap<T, SingleTriggerConfiguration<S, T, V>>()
    private val triggerTypeConfigurations = HashMap<Class<out T>, SingleTriggerConfiguration<S, T, V>>()
    private val substateConfigurations = HashMap<S, StateLevelConfiguration<S, T, V>>()
    private val entryActions = ArrayList<StateAction<S, T, V>>()
    private val exitActions = ArrayList<StateAction<S, T, V>>()

    override fun state(state: S,
                       init: StateConfiguration<S, T, V>.() -> Unit): StateConfiguration<S, T, V> =
            substateConfigurations.computeIfAbsent(state, {
                stateConfigurationFactory.createConfiguration(state, this).apply(init)
            })

    override fun on(trigger: T,
                    vararg triggers: T,
                    init: TriggerConfiguration<S, T, V>.() -> Unit) =
            if (triggers.isEmpty()) {
                triggerConfigurations.computeIfAbsent(trigger, { SingleTriggerReferenceConfiguration(this, state, trigger) }).apply(init)
            } else {
                GroupTriggerConfiguration(this, listOf(trigger, * triggers).map { on(it) }).apply(init)
            }

    @Suppress("UNCHECKED_CAST")
    override fun <P : T> onOneOf(triggerType: Class<out P>,
                                 vararg triggerTypes: Class<out P>,
                                 init: TriggerConfiguration<S, P, V>.() -> Unit) =
            if (triggerTypes.isEmpty()) {
                (triggerTypeConfigurations.computeIfAbsent(triggerType, { SingleTriggerTypeConfiguration(this, state, triggerType as Class<T>) }) as TriggerConfiguration<S, P, V>).apply(init)
            } else {
                (GroupTriggerConfiguration(this, listOf(triggerType, * triggerTypes).map { onOneOf(it) as TriggerConfiguration<S, T, V> }) as TriggerConfiguration<S, P, V>).apply(init)
            }

    override fun onEntry(action: StateAction<S, T, V>): StateConfiguration<S, T, V> {
        entryActions.add(action)
        return this
    }

    override fun <P : T> onEntryFromOneOf(triggerType: Class<out P>,
                                          action: StateAction<S, P, V>): StateConfiguration<S, T, V> {
        entryActions.add(object : StateAction<S, T, V> {
            @Suppress("UNCHECKED_CAST")
            override fun invoke(target: V, transition: Transition<S, T>) {
                if (triggerType.isInstance(transition.trigger)) action(target, Transition(transition.source, transition.destination,
                        transition.trigger as P))
            }
        })
        return this
    }

    override fun onEntryFrom(trigger: T,
                             action: StateAction<S, T, V>): StateConfiguration<S, T, V> {
        entryActions.add(object : StateAction<S, T, V> {
            override fun invoke(target: V, transition: Transition<S, T>) {
                if (transition.trigger == trigger) action(target, transition)
            }
        })
        return this
    }

    override fun onExit(action: StateAction<S, T, V>): StateConfiguration<S, T, V> {
        exitActions.add(action)
        return this
    }

    fun build(superstateRepresentation: StateRepresentation<S, T, V>?) =
            StateRepresentation(state, superstateRepresentation,
                    triggerConfigurations.map { it.value.build() } + triggerTypeConfigurations.map { it.value.build() },
                    entryActions.toList(), exitActions.toList(), substateConfigurations)
}
