package fr.guddy.kandroidmodular.fsm

import au.com.ds.ef.EasyFlow
import au.com.ds.ef.StateEnum
import au.com.ds.ef.StatefulContext
import au.com.ds.ef.call.ContextHandler


fun <T : StatefulContext> EasyFlow<T>.whenEnter(state: StateEnum, contextHandler: (context: T) -> Unit) {
    whenEnter<T>(
            state,
            ContextHandler { context ->
                contextHandler(context)
            }
    )
}

fun <T : StatefulContext> EasyFlow<T>.whenLeave(state: StateEnum, contextHandler: (context: T) -> Unit) {
    whenLeave<T>(
            state,
            ContextHandler { context ->
                contextHandler(context)
            }
    )
}