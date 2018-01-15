package fr.guddy.kandroidmodular.di

import fr.guddy.kandroidmodular.common.fsm.fsmModule
import fr.guddy.kandroidmodular.userinput.di.userInputModule
import fr.guddy.kandroidmodular.userrepos.di.userReposModule

val allModules = listOf(
        netModule,
        fsmModule,
        userInputModule,
        userReposModule
)