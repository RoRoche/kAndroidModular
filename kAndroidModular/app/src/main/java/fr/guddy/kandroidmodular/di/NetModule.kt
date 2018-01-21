package fr.guddy.kandroidmodular.di

import fr.guddy.kandroidmodular.userrepos.net.GitHubApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.kotlin.createRetrofitService

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
    return createRetrofitService {
        this.baseUrl = baseUrl
        this.client = client
        converterFactories = arrayListOf(GsonConverterFactory.create())
        callAdapterFactories = arrayListOf(RxJava2CallAdapterFactory.create())
    }
}

object Properties {
    const val SERVER_URL = "SERVER_URL"
}