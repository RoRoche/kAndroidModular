package fr.guddy.kandroidmodular.usecases

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.rossdanderson.stateless4k.StateMachine
import com.github.rossdanderson.stateless4k.config.StateMachineConfiguration
import fr.guddy.kandroidmodular.R
import fr.guddy.kandroidmodular.fsm.FsmViewModel
import fr.guddy.kandroidmodular.fsm.Model
import fr.guddy.kandroidmodular.usecases.userinput.UserInputFragment
import fr.guddy.kandroidmodular.usecases.userrepos.UserReposFragment
import org.koin.android.architecture.ext.getViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FsmViewModel
    private lateinit var stateMachine: StateMachine<State, Trigger, Model>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = getViewModel()
        startStateMachine()

        showUserInputFragment()
    }

    private fun showUserInputFragment() {
        if (supportFragmentManager.findFragmentById(R.id.ActivityMain_ViewGroup_Container) == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.ActivityMain_ViewGroup_Container, UserInputFragment.newInstance())
                    .commit()
        }
    }

    fun showUserReposFragment(user: String) {
        stateMachine.fire(Trigger.OnUserFilled(user))
        supportFragmentManager.beginTransaction()
                .replace(R.id.ActivityMain_ViewGroup_Container, UserReposFragment.newInstance(user))
                .addToBackStack(null)
                .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            stateMachine.fire(Trigger.OnBackPressed())
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun startStateMachine() {
        val stateMachineModel = StateMachineConfiguration.create<State, Trigger, Model> {
            state(State.WaitingUserInput) {
                onEntry(Model::onEntryState)
                onExit(Model::onExitState)
                onOneOf(Trigger.OnUserFilled::class.java) {
                    transitionTo(State.ShowingUserRepos) {
                        onEntry(Model::onEntryState)
                        onExit(Model::onExitState)
                        onOneOf(Trigger.OnBackPressed::class.java) {
                            transitionTo(State.WaitingUserInput)
                        }
                    }
                }
            }
        }.build()
        stateMachine = stateMachineModel.start(Model(), State.ShowingUserRepos)

        viewModel.context.observe(this, Observer {
            it?.let { context ->
                stateMachine = stateMachineModel.start(
                        context.model,
                        context.model.currentState!!,
                        context.performEntryActionsForInitialState
                )
            }
        })
    }

    enum class State {
        WaitingUserInput, ShowingUserRepos
    }

    /**
     * The classes we're going to use instances of for our triggers.
     */
    sealed class Trigger {
        class OnUserFilled(val user: String) : Trigger()
        class OnBackPressed : Trigger()
    }
}