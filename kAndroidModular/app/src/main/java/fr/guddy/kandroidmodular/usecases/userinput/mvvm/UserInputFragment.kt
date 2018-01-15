package fr.guddy.kandroidmodular.usecases.userinput.mvvm


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.validation.Validator
import fr.guddy.kandroidmodular.R
import fr.guddy.kandroidmodular.databinding.FragmentUserInputBinding
import fr.guddy.kandroidmodular.di.getViewModelFromActivity
import fr.guddy.kandroidmodular.fsm.FsmViewModel
import fr.guddy.kandroidmodular.mvvm.observe
import fr.guddy.kandroidmodular.usecases.userinput.fsm.UserFilled
import fr.guddy.kandroidmodular.usecases.userinput.fsm.UserInputResult
import fr.guddy.kandroidmodular.usecases.userinput.fsm.userInputResult
import org.koin.android.architecture.ext.getViewModel


class UserInputFragment : Fragment() {

    private lateinit var viewModel: UserInputViewModel
    private lateinit var fsmViewModel: FsmViewModel
    private lateinit var binding: FragmentUserInputBinding
    private lateinit var validator: Validator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        viewModel = getViewModel()
        fsmViewModel = getViewModelFromActivity()
        binding.viewModel = viewModel
        arguments?.getString(ARG_PRE_FILLED_USER)?.let { user ->
            viewModel.model.user.set(user)
        }
        binding.model = viewModel.model
        viewModel.onSelectEvent.observe(this) { user ->
            onSelect(user)
        }
    }

    private fun onSelect(user: String) {
        fsmViewModel.flowContext.userInputResult = UserInputResult(user)
        fsmViewModel.trigger(UserFilled)
    }

    companion object Factory {
        private val ARG_PRE_FILLED_USER = "PRE_FILLED_USER"

        fun newInstance(): UserInputFragment {
            val fragment = UserInputFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(user: String): UserInputFragment {
            val fragment = UserInputFragment()
            val args = Bundle()
            args.putString(ARG_PRE_FILLED_USER, user)
            fragment.arguments = args
            return fragment
        }
    }

}
