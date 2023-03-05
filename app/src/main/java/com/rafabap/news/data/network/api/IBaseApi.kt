package com.rafabap.news.data.network.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface IBaseApi {
    fun build(): Retrofit
    fun getBuilder(): OkHttpClient.Builder
}