package com.rafabap.news.data.network.api

import com.rafabap.news.data.model.ArticleResponse
import com.rafabap.news.data.network.service.INewsApiRestService
import io.reactivex.rxjava3.core.Single

class NewsApi(baseApi: IBaseApi) {

    private val apiRestClient: INewsApiRestService

    init {
        val retrofit = baseApi.build()
        apiRestClient = retrofit.create(INewsApiRestService::class.java)
    }

    fun getTopHeadlineNews(source: String): Single<List<ArticleResponse>> {
        return apiRestClient.getTopHeadlines(source)
            .map {
                it.articles ?: emptyList()
            }
    }
}
