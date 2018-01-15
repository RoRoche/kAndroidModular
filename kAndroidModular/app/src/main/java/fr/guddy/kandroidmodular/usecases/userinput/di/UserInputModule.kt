package fr.guddy.kandroidmodular.usecases.userinput.di

import fr.guddy.kandroidmodular.usecases.userinput.mvvm.UserInputViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val userInputModule = applicationContext {
    viewModel {
        UserInputViewModel()
    }
}