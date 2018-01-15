package fr.guddy.kandroidmodular.usecases.userinput.fsm

import fr.guddy.kandroidmodular.common.fsm.FsmContext
import fr.guddy.kandroidmodular.common.fsm.FsmEvent
import fr.guddy.kandroidmodular.common.fsm.FsmState


object WaitingUserInput : FsmState

object UserFilled : FsmEvent

private val _resultKey = "UserInputResult"

var FsmContext.userInputResult: UserInputResult
    get() = args.getParcelable(_resultKey)
    set(value) {
        args.putParcelable(_resultKey, value)
    }

fun FsmContext.clearUserInputResult() {
    args.remove(_resultKey)
}