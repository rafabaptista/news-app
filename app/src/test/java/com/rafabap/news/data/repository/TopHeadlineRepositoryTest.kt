package com.rafabap.news.data.repository

import android.annotation.SuppressLint
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rafabap.news.data.model.ArticleResponse
import com.rafabap.news.data.model.SourceResponse
import com.rafabap.news.data.network.api.NewsApi
import com.rafabap.news.domain.repository.TopHeadlineRepository
import com.rafabap.news.presentation.util.RxSchedulersOverrideRule
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TopHeadlineRepositoryTest {

    @get:Rule
    var schedulersOverrideRule: RxSchedulersOverrideRule = RxSchedulersOverrideRule()
    @Mock
    lateinit var newsApi: NewsApi

    private lateinit var repository: TopHeadlineRepository

    private val data = ArticleResponse(
        source = SourceResponse("id", "name"),
        author = "author",
        title = "title",
        description = "description",
        url = "url",
        urlToImage = "urlToImage",
        publishedAt = "publishedAt",
        content = "content"
    )

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = TopHeadlineRepository(newsApi)
    }

    @SuppressLint("CheckResult")
    @Test
    fun `test that repository is receiving valid data from api successfully`() {
        whenever(newsApi.getTopHeadlineNews(anyString())).thenAnswer {
            Single.just(data)
        }

        repository.getTopHeadlineNews(SOURCE_VALUE).test()
        verify(newsApi).getTopHeadlineNews(SOURCE_VALUE)
    }

    private companion object {
        const val SOURCE_VALUE = "us"
    }
}