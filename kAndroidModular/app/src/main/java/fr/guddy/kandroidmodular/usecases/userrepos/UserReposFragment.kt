package fr.guddy.kandroidmodular.usecases.userrepos


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fr.guddy.kandroidmodular.R
import fr.guddy.kandroidmodular.databinding.FragmentUserReposBinding

class UserReposFragment : Fragment() {

    private lateinit var model: UserReposModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            model = UserReposModel(arguments.getString(ARG_USER))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentUserReposBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_user_repos,
                container,
                false
        )
        binding.model = model
        return binding.root
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
