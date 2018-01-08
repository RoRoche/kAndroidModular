package fr.guddy.kandroidmodular

import fr.guddy.kandroidmodular.di.netModule
import fr.guddy.kandroidmodular.fsm.fsmModule
import fr.guddy.kandroidmodular.usecases.userinput.userInputModule
import fr.guddy.kandroidmodular.usecases.userrepos.userReposModule

val allModules = listOf(
        netModule,
        fsmModule,
        userInputModule,
        userReposModule
)