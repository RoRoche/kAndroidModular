package fr.guddy.kandroidmodular.common.fsm

import au.com.ds.ef.StateEnum

interface FsmState : StateEnum {
    override fun name(): String = javaClass.name
}