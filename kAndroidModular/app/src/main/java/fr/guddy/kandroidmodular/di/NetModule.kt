package fr.guddy.kandroidmodular.di

import fr.guddy.kandroidmodular.net.GitHubApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val netModule = applicationContext {
    bean {
        createOkHttpClient()
    }
    bean {
        createGitHubApi(
                get(),
                getProperty(Properties.SERVER_URL)
        )
    }
}

fun createOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
}

fun createGitHubApi(client: OkHttpClient, baseUrl: String): GitHubApi {
    val retrofit = Retrofit.Builder()
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    return retrofit.create(GitHubApi::class.java)
}

object Properties {
    const val SERVER_URL = "SERVER_URL"
}