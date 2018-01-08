package fr.guddy.kandroidmodular.fsm

import com.github.rossdanderson.stateless4k.Transition
import fr.guddy.kandroidmodular.usecases.MainActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class Model {
    var user: String? = null
    var currentState: MainActivity.State? = null
    private val logger = AnkoLogger(this.javaClass)

    fun onEntryState(transition: Transition<MainActivity.State, MainActivity.Trigger>) {
        logger.info { "onEntryState: $transition" }
        currentState = transition.destination
    }

    fun onExitState(transition: Transition<MainActivity.State, MainActivity.Trigger>) {
        logger.info { "onExitState: $transition" }
    }
}