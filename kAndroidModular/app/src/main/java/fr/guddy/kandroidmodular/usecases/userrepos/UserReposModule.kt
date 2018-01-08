package fr.guddy.kandroidmodular.usecases.userrepos

import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val userReposModule = applicationContext {
    viewModel {
        UserReposViewModel(get())
    }
}