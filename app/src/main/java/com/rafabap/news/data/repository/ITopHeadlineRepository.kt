package com.rafabap.news.data.repository

import com.rafabap.news.data.model.ArticleResponse
import com.rafabap.news.data.model.SourceResponse
import io.reactivex.rxjava3.core.Single

interface ITopHeadlineRepository {
    fun getTopHeadlineNews(source: String): Single<List<ArticleResponse>>
}