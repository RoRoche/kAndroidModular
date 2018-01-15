package fr.guddy.kandroidmodular.userrepos.net

import fr.guddy.kandroidmodular.userrepos.net.dto.Repo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubApi {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Single<List<Repo>>
}