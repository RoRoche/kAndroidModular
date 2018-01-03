package fr.guddy.kandroidmodular.usecases.userinput


import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.validation.Validator
import fr.guddy.kandroidmodular.usecases.MainActivity
import fr.guddy.kandroidmodular.R
import fr.guddy.kandroidmodular.databinding.FragmentUserInputBinding


class UserInputFragment : Fragment() {

    private lateinit var viewModel: UserInputViewModel
    private lateinit var binding: FragmentUserInputBinding
    private lateinit var validator: Validator

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_user_input,
                container,
                false
        )
        validator = Validator(binding)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserInputViewModel::class.java)
        binding.model = viewModel.model
        binding.buttonLoadRepos.setOnClickListener { onClickLoadRepos() }
    }

    private fun onClickLoadRepos() {
        if (validator.validate()) {
            (activity as MainActivity).showUserReposFragment(
                    viewModel.model.user.get()
            )
        }
    }

    companion object Factory {
        fun newInstance(): UserInputFragment {
            val fragment = UserInputFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
