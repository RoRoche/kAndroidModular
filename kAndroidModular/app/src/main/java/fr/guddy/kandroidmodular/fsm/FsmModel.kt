package fr.guddy.kandroidmodular.fsm

data class FsmModel(
        val forceEnterInitialState: Boolean = false,
        val flowContext: FsmContext = FsmContext()
)