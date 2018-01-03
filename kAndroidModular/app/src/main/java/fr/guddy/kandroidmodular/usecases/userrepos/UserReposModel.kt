package fr.guddy.kandroidmodular.usecases.userrepos

import fr.guddy.kandroidmodular.net.dto.Repo

data class UserReposModel(
        val user: String,
        val loading: Boolean = false,
        val repos: List<Repo> = emptyList(),
        val error: Throwable? = null
) {

    fun isLoading() = loading

    fun isError() = !loading && error != null

    fun isEmpty() = !loading && error == null && repos.isEmpty()

    fun isContent() = !loading && error == null && repos.isNotEmpty()

    fun reposAsString() = repos.toString()
}