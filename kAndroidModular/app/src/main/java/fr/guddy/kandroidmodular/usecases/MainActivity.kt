package fr.guddy.kandroidmodular.usecases

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.guddy.kandroidmodular.R
import fr.guddy.kandroidmodular.fsm.FsmViewModel
import fr.guddy.kandroidmodular.usecases.userinput.UserInputFragment
import fr.guddy.kandroidmodular.usecases.userrepos.UserReposFragment
import org.koin.android.architecture.ext.getViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FsmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = getViewModel()

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
        supportFragmentManager.beginTransaction()
                .replace(R.id.ActivityMain_ViewGroup_Container, UserReposFragment.newInstance(user))
                .addToBackStack(null)
                .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            // TODO
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}