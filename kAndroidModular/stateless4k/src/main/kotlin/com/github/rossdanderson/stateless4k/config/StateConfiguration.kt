package com.github.rossdanderson.stateless4k.config

import com.github.rossdanderson.stateless4k.Transition
import com.github.rossdanderson.stateless4k.interfaces.StateAction
import kotlin.reflect.KClass

@Suppress("unused")
interface StateConfiguration<S, T : Any, V> : SuperstateConfiguration<S, T, V> {

    fun on(trigger: T,
           vararg triggers: T): TriggerConfiguration<S, T, V> =
            on(trigger, *triggers, init = { })

    fun on(trigger: T,
           vararg triggers: T,
           init: TriggerConfiguration<S, T, V>.() -> Unit): TriggerConfiguration<S, T, V>

    fun <P : T> onOneOf(triggerType: Class<out P>,
                        vararg triggerTypes: Class<out P>): TriggerConfiguration<S, P, V> =
            onOneOf(triggerType, *triggerTypes, init = { })

    fun <P : T> onOneOf(triggerType: Class<out P>,
                        vararg triggerTypes: Class<out P>,
                        init: TriggerConfiguration<S, P, V>.() -> Unit): TriggerConfiguration<S, P, V>

    // TODO add KClass types

    /**
     * Specify an action that will execute when transitioning into the configured state
     * [action] Action to execute, providing details of the transition
     * @return The receiver
     */
    fun onEntry(action: StateAction<S, T, V>): StateConfiguration<S, T, V>

    fun StateConfiguration<S, T, V>.onEntry(action: (V, Transition<S, T>) -> Unit): StateConfiguration<S, T, V> {
        return onEntry(createStateAction(action))
    }

    /**
     * Specify an action that will execute when transitioning into the configured state from an instance of the provided
     * [triggerType]
     * @param triggerType The type of trigger by which the state must be entered in order for the action to execute
     * @param action      Action to execute, providing details of the transition
     * @return The receiver
     */
    fun <P : T> onEntryFromOneOf(triggerType: Class<out P>,
                                 action: StateAction<S, P, V>): StateConfiguration<S, T, V>

    fun <P : T> StateConfiguration<S, T, V>.onEntryFromOneOf(triggerType: Class<out P>,
                                                             action: (V, Transition<S, P>) -> Unit): StateConfiguration<S, T, V> =
            onEntryFromOneOf(triggerType, createStateAction(action))

    fun <P : T> onEntryFromOneOf(triggerType: KClass<out P>,
                                 action: StateAction<S, P, V>): StateConfiguration<S, T, V> =
            onEntryFromOneOf(triggerType.java, action)

    fun <P : T> StateConfiguration<S, T, V>.onEntryFromOneOf(triggerType: KClass<out P>,
                                                             action: (V, Transition<S, P>) -> Unit): StateConfiguration<S, T, V> =
            onEntryFromOneOf(triggerType.java, createStateAction(action))

    /**
     * Specify an action that will execute when transitioning into the configured state from the provided [trigger]
     * @param trigger     The trigger by which the state must be entered in order for the action to execute
     * @param action      Action to execute, providing details of the transition
     * @return The receiver
     */
    fun onEntryFrom(trigger: T,
                    action: StateAction<S, T, V>): StateConfiguration<S, T, V>

    fun StateConfiguration<S, T, V>.onEntryFrom(trigger: T,
                                                action: (V, Transition<S, T>) -> Unit): StateConfiguration<S, T, V> {
        return onEntryFrom(trigger, createStateAction(action))
    }

    /**
     * Specify an action that will execute when transitioning from the configured state
     * @param action Action to execute
     * @return The receiver
     */
    fun onExit(action: StateAction<S, T, V>): StateConfiguration<S, T, V>

    fun StateConfiguration<S, T, V>.onExit(action: (V, Transition<S, T>) -> Unit): StateConfiguration<S, T, V> {
        return onExit(createStateAction(action))
    }

    fun <P : T> createStateAction(action: (V, Transition<S, P>) -> Unit): StateAction<S, P, V> =
            object : StateAction<S, P, V> {
                override fun invoke(target: V, transition: Transition<S, P>) {
                    action.invoke(target, transition)
                }
            }
}
