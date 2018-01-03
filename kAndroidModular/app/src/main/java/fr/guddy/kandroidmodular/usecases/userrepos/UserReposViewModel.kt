package fr.guddy.kandroidmodular.usecases.userrepos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import fr.guddy.kandroidmodular.net.GitHubApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserReposViewModel : ViewModel() {
    val model: MutableLiveData<UserReposModel> = MutableLiveData()
    val gitHubApi by lazy {
        GitHubApi.create()
    }
    var disposable: Disposable? = null

    fun updateModelUser(user: String) {
        if (model.value == null || model.value!!.user.let { user.compareTo(it) } != 0) {
            loadRepos(user)
        }
    }

    fun loadRepos(user: String) {
        model.value = UserReposModel(
                user = user,
                loading = true
        )
        disposable = gitHubApi.listRepos(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            model.value = UserReposModel(
                                    user = user,
                                    repos = result
                            )
                            dispose()
                        },
                        { error ->
                            model.value = UserReposModel(
                                    user = user,
                                    error = error
                            )
                            dispose()
                        }
                )
    }

    override fun onCleared() {
        dispose()
        super.onCleared()
    }

    private fun dispose() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        disposable = null
    }
}