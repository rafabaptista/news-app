package com.rafabap.news.presentation.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rafabap.news.domain.model.Article
import com.rafabap.news.domain.usecase.TopHeadlineUseCase
import com.rafabap.news.presentation.util.extension.*
import com.rafabap.news.presentation.view.NetworkState

class TopHeadlineViewModel(private val useCase: TopHeadlineUseCase) {

    var networkState = MutableLiveData<NetworkState>()
    val articleList = MutableLiveData<List<Article>>()

    fun loadTopHeadlineArticles(source: String) {
        networkState.running()
        useCase.fetchTopHeadlineNews(source)
            .doSubscribe(
                { response ->
                    updateData(response)
                }, {
                    networkState.error(ERROR_LOADING_NEWS)
                    this.articleList.postValue(emptyList())
                }
            )

    }

    fun getSourceName() = articleList.value?.firstOrNull()?.sourceName ?: ""

    private fun updateData(articleList: List<Article>) {
        if (articleList.isEmpty()) {
            networkState.empty()
        } else {
            networkState.success()
        }
        this.articleList.postValue(articleList)
    }

    companion object {
        const val ERROR_LOADING_NEWS = "ERROR_LOADING_NEWS"
    }
}