package fr.guddy.kandroidmodular.fsm

data class FsmContext(val performEntryActionsForInitialState: Boolean = false, val model: Model)