package fr.guddy.kandroidmodular.userrepos.di

import fr.guddy.kandroidmodular.userrepos.mvvm.UserReposViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val userReposModule = applicationContext {
    viewModel {
        UserReposViewModel(get())
    }
}
