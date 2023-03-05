package com.rafabap.news.domain.usecase

import com.rafabap.news.data.mapper.ArticleMapper
import com.rafabap.news.domain.model.Article
import com.rafabap.news.domain.repository.ITopHeadlineRepository
import io.reactivex.rxjava3.core.Single

class TopHeadlineUseCase(private val repository: ITopHeadlineRepository,
                         private val articleMapper: ArticleMapper) : BaseUseCase() {

    fun fetchTopHeadlineNews(source: String): Single<List<Article>> {
        return repository.getTopHeadlineNews(source)
            .map {
                articleMapper.map(it)
            }
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
    }
}