package fr.guddy.kandroidmodular.common.fsm

import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val fsmModule = applicationContext {
    viewModel {
        FsmViewModel()
    }
}

object BackPressed : FsmEvent