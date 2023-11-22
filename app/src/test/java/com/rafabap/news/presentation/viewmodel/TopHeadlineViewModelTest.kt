package com.rafabap.news.presentation.viewmodel

import com.rafabap.news.base.BaseTest
import com.rafabap.news.base.TestSuite
import com.rafabap.news.base.mock.TestConstants
import com.rafabap.news.base.mock.endpoint.ResponseHandler
import com.rafabap.news.presentation.view.NetworkState
import com.rafabap.news.presentation.viewmodel.TopHeadlineViewModel
import com.rafabap.news.common.util.extension.getOrAwaitValue
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.koin.test.inject
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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

        val testData = viewModel.articleList.getOrAwaitValue()
        assertNotNull(testData)
        assert(testData.isNotEmpty())
        assertEquals(8, testData.size)

        val testDataItem = testData.first()
        assertNotNull(testDataItem)
        assertEquals("BBC News", testDataItem.author)
        assertEquals("Mocked Test Json title", testDataItem.title)
        assertEquals(true, testDataItem.hasDescription)
        assertEquals("Mocked Test Json description", testDataItem.description)
        assertEquals("http://test.url", testDataItem.url)
        assertEquals(true, testDataItem.hasImageUrl)
        assertEquals("https://test-image.url.png", testDataItem.urlToImage)
        assertEquals("2023-03-02T17:52:19.637784Z", testDataItem.publishedAt)
        assertEquals(true, testDataItem.hasContent)
        assertEquals("Mocked Test Json content", testDataItem.content)

        val testNetworkStatus = viewModel.networkState.getOrAwaitValue()
        assertNotNull(testNetworkStatus)
        assertEquals(NetworkState.NetworkStateStatus.SUCCESS, testNetworkStatus.status)

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

        val testData = viewModel.articleList.getOrAwaitValue()
        assertNotNull(testData)
        assert(testData.isEmpty())
        assertEquals(0, testData.size)

        val testNetworkStatus = viewModel.networkState.getOrAwaitValue()
        assertNotNull(testNetworkStatus)
        assertEquals(NetworkState.NetworkStateStatus.ERROR, testNetworkStatus.status)

        assertEquals("", viewModel.getSourceName())
    }

    @Test
    fun `test that top headlines gets any error successfully`() {
        TestSuite.mockUrl(TestConstants.TOP_HEADLINES_URL).throwConnectionError().apply()

        viewModel.loadTopHeadlineArticles(anyString())

        val testData = viewModel.articleList.getOrAwaitValue()
        assertNotNull(testData)
        assert(testData.isEmpty())
        assertEquals(0, testData.size)

        val testNetworkStatus = viewModel.networkState.getOrAwaitValue()
        assertNotNull(testNetworkStatus)
        assertEquals(NetworkState.NetworkStateStatus.ERROR, testNetworkStatus.status)

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

        val testData = viewModel.articleList.getOrAwaitValue()
        assertNotNull(testData)
        assert(testData.isEmpty())
        assertEquals(0, testData.size)

        val testNetworkStatus = viewModel.networkState.getOrAwaitValue()
        assertNotNull(testNetworkStatus)
        assertEquals(NetworkState.NetworkStateStatus.EMPTY, testNetworkStatus.status)

        assertEquals("", viewModel.getSourceName())
    }
}