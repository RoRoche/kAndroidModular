package fr.guddy.kandroidmodular.usecases.userrepos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import fr.guddy.kandroidmodular.net.GitHubApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class UserReposViewModel(val gitHubApi: GitHubApi) : ViewModel() {
    val model: MutableLiveData<UserReposModel> = MutableLiveData()
    private var disposable: Disposable? = null

    fun updateModelUser(user: String) {
        if (model.value == null || model.value!!.user.let { user.compareTo(it) } != 0) {
            loadRepos(user)
        }
    }

    private fun loadRepos(user: String) {
        model.value = UserReposModel(
                user = user,
                loading = true
        )
        dispose()
        disposable = gitHubApi.listRepos(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            model.value = UserReposModel(
                                    user = user,
                                    repos = it
                            )
                            dispose()
                        },
                        onError = {
                            model.value = UserReposModel(
                                    user = user,
                                    error = it
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