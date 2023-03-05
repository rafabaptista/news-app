package com.rafabap.news.presentation.viewmodel

import com.rafabap.news.base.BaseTest
import com.rafabap.news.base.TestSuite
import com.rafabap.news.base.mock.TestConstants
import com.rafabap.news.base.mock.endpoint.ResponseHandler
import com.rafabap.news.presentation.view.NetworkState
import com.rafabap.news.presentation.view.viewmodel.TopHeadlineViewModel
import com.rafabap.news.presentation.util.extension.getOrAwaitValue
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.koin.test.inject
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class TopHeadlineViewModelTest : BaseTest() {

    private lateinit var viewModel: TopHeadlineViewModel

    @Before
    fun setupTest() {
        MockitoAnnotations.initMocks(this)
        viewModel = TestSuite.inject<TopHeadlineViewModel>().value
    }

    @Test
    fun `test that top headlines are received successfully`() {
        TestSuite.mockUrl(TestConstants.TOP_HEADLINES_URL).body(object : ResponseHandler {
            override fun getResponse(request: Request, path: String): String {
                return TestSuite.returnJsonData(TestConstants.TOP_HEADLINES_URL)
            }
        }).apply()

        viewModel.loadTopHeadlineArticles(anyString())

        assertEquals(8, viewModel.articleList.getOrAwaitValue()?.size)
        assertEquals(NetworkState.NetworkStateStatus.SUCCESS, viewModel.networkState.getOrAwaitValue()?.status)
        assertEquals(viewModel.getSourceName(), "BBC News")
    }

    @Test
    fun `test that top headlines gets api key error successfully`() {
        TestSuite.mockUrl(TestConstants.TOP_HEADLINES_URL)
            .code(401)
            .body(object : ResponseHandler {
                override fun getResponse(request: Request, path: String): String {
                    return TestSuite.returnJsonData("${TestConstants.TOP_HEADLINES_URL}_error")
                }
            })
            .apply()

        viewModel.loadTopHeadlineArticles(anyString())

        assertEquals(0, viewModel.articleList.getOrAwaitValue()?.size)
        assertEquals(NetworkState.NetworkStateStatus.ERROR, viewModel.networkState.getOrAwaitValue()?.status)
        assertEquals("", viewModel.getSourceName())
    }

    @Test
    fun `test that top headlines gets any error successfully`() {
        TestSuite.mockUrl(TestConstants.TOP_HEADLINES_URL).throwConnectionError().apply()

        viewModel.loadTopHeadlineArticles(anyString())

        assertEquals(0, viewModel.articleList.getOrAwaitValue()?.size)
        assertEquals(NetworkState.NetworkStateStatus.ERROR, viewModel.networkState.getOrAwaitValue()?.status)
        assertEquals("", viewModel.getSourceName())
    }

    @Test
    fun `test that top headlines is empty successfully`() {
        TestSuite.mockUrl(TestConstants.TOP_HEADLINES_URL)
            .body(object : ResponseHandler {
                override fun getResponse(request: Request, path: String): String {
                    return TestSuite.returnJsonData("${TestConstants.TOP_HEADLINES_URL}_empty")
                }
            })
            .apply()

        viewModel.loadTopHeadlineArticles(anyString())

        assertEquals(0, viewModel.articleList.getOrAwaitValue()?.size)
        assertEquals(NetworkState.NetworkStateStatus.EMPTY, viewModel.networkState.getOrAwaitValue()?.status)
        assertEquals("", viewModel.getSourceName())
    }
}