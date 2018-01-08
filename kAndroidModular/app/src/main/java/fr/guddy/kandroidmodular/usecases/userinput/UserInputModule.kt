package fr.guddy.kandroidmodular.usecases.userinput

import fr.guddy.kandroidmodular.fsm.State
import fr.guddy.kandroidmodular.fsm.Trigger
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val userInputModule = applicationContext {
    viewModel {
        UserInputViewModel()
    }
}

object WaitingUserInput : State

data class OnUserFilled(val user: String) : Trigger
