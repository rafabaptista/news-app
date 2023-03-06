package com.rafabap.news.domain.repository

import com.rafabap.news.data.model.ArticleResponse
import com.rafabap.news.data.network.api.NewsApi
import io.reactivex.rxjava3.core.Single

class TopHeadlineRepository(private val newsApiRestClient: NewsApi) : ITopHeadlineRepository {

    override fun getTopHeadlineNews(source: String): Single<List<ArticleResponse>> {
        return newsApiRestClient.getTopHeadlineNews(source)
    }
}