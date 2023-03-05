package com.rafabap.news.data.network.api

import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Companion.invoke
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class BaseApi: IBaseApi {

    companion object {
        private const val API_TIMEOUT_SECONDS = 30
        private const val API_KEY = "48ce005846bb4c5eb972bb87b4e12e49" //TODO extract to external buildconfig
        private const val BASE_URL = "https://newsapi.org/v2/"
    }

    override fun build(): Retrofit {
        val baseUrl = BASE_URL
        val timeout = API_TIMEOUT_SECONDS

        val builder = getBuilder()

        val clientBuilder = builder
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(timeout.toLong(), TimeUnit.SECONDS)

        val client = clientBuilder.build()
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )

        return retrofitBuilder.build()
    }

    override fun getBuilder() = OkHttpClient.Builder()
        .addInterceptor(generalInterceptor())
        .addInterceptor(logInterceptor())

    private fun generalInterceptor(): Interceptor =
        invoke { chain ->
            val original = chain.request()

            val originalHttpUrl = original.url

            val url: HttpUrl = originalHttpUrl.newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .build()

            val builder = original.newBuilder()
                .url(url)
                .header("Content-Type", "application/json")
                .method(original.method, original.body)

            val request = builder.build()
            chain.proceed(request)
        }

    private fun logInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}