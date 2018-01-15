package fr.guddy.kandroidmodular.usecases.userrepos.mvvm


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silmood.bindapter.BR
import com.silmood.bindapter.Bindapter
import fr.guddy.kandroidmodular.R
import fr.guddy.kandroidmodular.databinding.FragmentUserReposBinding
import fr.guddy.kandroidmodular.mvvm.observe
import fr.guddy.kandroidmodular.net.dto.Repo
import org.koin.android.architecture.ext.getViewModel


class UserReposFragment : Fragment() {

    private lateinit var viewModel: UserReposViewModel
    private lateinit var binding: FragmentUserReposBinding
    private lateinit var bindapter: Bindapter<Repo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_user_repos,
                container,
                false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = getViewModel()
        viewModel.model.observe(this) { model ->
            binding.model = model
            bindapter.replaceItems(model.repos)
        }
        arguments?.getString(ARG_USER)?.let { user ->
            viewModel.updateModelUser(user)
        }
        bindapter = Bindapter(R.layout.user_repo_item, BR.repo)
        binding.listRepos.layoutManager = LinearLayoutManager(context)
        binding.listRepos.adapter = bindapter
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
