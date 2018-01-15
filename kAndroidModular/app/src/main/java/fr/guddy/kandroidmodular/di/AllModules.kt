package fr.guddy.kandroidmodular.di

import fr.guddy.kandroidmodular.fsm.fsmModule
import fr.guddy.kandroidmodular.usecases.userinput.di.userInputModule
import fr.guddy.kandroidmodular.usecases.userrepos.di.userReposModule

val allModules = listOf(
        netModule,
        fsmModule,
        userInputModule,
        userReposModule
)