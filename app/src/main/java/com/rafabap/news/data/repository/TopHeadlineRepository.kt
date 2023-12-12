package com.rafabap.news.data.repository

import com.rafabap.news.data.model.ArticleResponse
import com.rafabap.news.data.network.api.NewsApi
import com.rafabap.news.domain.repository.ITopHeadlineRepository
import io.reactivex.rxjava3.core.Single

class TopHeadlineRepository(private val newsApiRestClient: NewsApi) : ITopHeadlineRepository {

    override fun getTopHeadlineNews(source: String): Single<List<ArticleResponse>> {
        return newsApiRestClient.getTopHeadlineNews(source)
    }
}