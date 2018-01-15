package fr.guddy.kandroidmodular.common.fsm

data class FsmModel(
        val forceEnterInitialState: Boolean = false,
        val flowContext: FsmContext = FsmContext()
)