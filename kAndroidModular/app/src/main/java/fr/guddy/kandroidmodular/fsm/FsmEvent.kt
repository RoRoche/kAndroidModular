package fr.guddy.kandroidmodular.fsm

import au.com.ds.ef.EventEnum


interface FsmEvent : EventEnum {
    override fun name(): String = javaClass.name
}