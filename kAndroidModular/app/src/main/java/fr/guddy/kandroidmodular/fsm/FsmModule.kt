package fr.guddy.kandroidmodular.fsm

import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val fsmModule = applicationContext {
    viewModel {
        FsmViewModel()
    }
}

class OnBackPressed : Trigger