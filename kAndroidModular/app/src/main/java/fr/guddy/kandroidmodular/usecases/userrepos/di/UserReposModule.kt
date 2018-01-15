package fr.guddy.kandroidmodular.usecases.userrepos.di

import fr.guddy.kandroidmodular.usecases.userrepos.mvvm.UserReposViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val userReposModule = applicationContext {
    viewModel {
        UserReposViewModel(get())
    }
}
