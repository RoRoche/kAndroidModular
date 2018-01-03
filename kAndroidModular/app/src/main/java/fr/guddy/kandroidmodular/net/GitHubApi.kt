package fr.guddy.kandroidmodular.net

import fr.guddy.kandroidmodular.net.dto.Repo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubApi {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Observable<List<Repo>>
}