package fr.guddy.kandroidmodular.userinput.mvvm


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.guddy.kandroidmodular.common.di.getViewModelFromActivity
import fr.guddy.kandroidmodular.common.fsm.FsmViewModel
import fr.guddy.kandroidmodular.common.mvvm.observe
import fr.guddy.kandroidmodular.userinput.R
import fr.guddy.kandroidmodular.userinput.databinding.FragmentUserInputBinding
import fr.guddy.kandroidmodular.userinput.fsm.UserFilled
import fr.guddy.kandroidmodular.userinput.fsm.UserInputResult
import fr.guddy.kandroidmodular.userinput.fsm.userInputResult
import org.koin.android.architecture.ext.getViewModel


class UserInputFragment : Fragment() {

    private lateinit var viewModel: UserInputViewModel
    private lateinit var fsmViewModel: FsmViewModel
    private lateinit var binding: FragmentUserInputBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_user_input,
                container,
                false
        )
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
        if (TextUtils.isEmpty(user)) {
            binding.editTextUser.error = getString(R.string.empty_user)
        } else {
            fsmViewModel.flowContext.userInputResult = UserInputResult(user)
            fsmViewModel.trigger(UserFilled)
        }
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
