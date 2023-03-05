package com.rafabap.news.data.network.service

import com.rafabap.news.data.model.TopHeadlineServiceResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface INewsApiRestService {

    @GET("top-headlines")
    fun getTopHeadlines(@Query("sources") source: String): Single<TopHeadlineServiceResponse>

}