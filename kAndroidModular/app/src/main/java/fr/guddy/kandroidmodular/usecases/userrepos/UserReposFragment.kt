package fr.guddy.kandroidmodular.usecases.userrepos


import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.guddy.kandroidmodular.R
import fr.guddy.kandroidmodular.databinding.FragmentUserReposBinding
import org.koin.android.architecture.ext.getViewModel


class UserReposFragment : Fragment() {

    private lateinit var viewModel: UserReposViewModel
    private lateinit var binding: FragmentUserReposBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_user_repos,
                container,
                false
        )
        binding.textViewRepos.movementMethod = ScrollingMovementMethod()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()
        viewModel.model.observe(
                this,
                Observer<UserReposModel> { model ->
                    binding.model = model
                }
        )
        arguments?.getString(ARG_USER)?.let {
            viewModel.updateModelUser(it)
        }
    }

    companion object Factory {
        private val ARG_USER = "user"

        fun newInstance(user: String): UserReposFragment {
            val fragment = UserReposFragment()
            val args = Bundle()
            args.putString(ARG_USER, user)
            fragment.arguments = args
            return fragment
        }
    }

}
