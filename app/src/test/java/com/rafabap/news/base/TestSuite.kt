package com.rafabap.news.base

import com.rafabap.news.base.mock.FileUtils
import com.rafabap.news.base.mock.MockedBaseApi
import com.rafabap.news.base.mock.endpoint.EndpointMock
import com.rafabap.news.data.network.api.IBaseApi
import com.rafabap.news.data.network.api.NewsApi
import com.rafabap.news.data.repository.ITopHeadlineRepository
import com.rafabap.news.data.repository.TopHeadlineRepository
import com.rafabap.news.common.di.mapperModules
import com.rafabap.news.common.di.useCaseModules
import com.rafabap.news.common.di.viewModelModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mock


object TestSuite : KoinTest {

    @Mock
    lateinit var baseApiRetrofit: MockedBaseApi

    fun mockUrl(url: String): EndpointMock {
        var urlReceived = url
        if (!urlReceived.startsWith("/") && !urlReceived.startsWith("http")) {
            urlReceived = "/$urlReceived"
        }
        return EndpointMock(urlReceived, baseApiRetrofit)
    }

    private fun clearEndpointMocks() {
        baseApiRetrofit.clearMocks()
    }

    private fun initKoin() {
        try {
            getKoin()
        } catch (e: IllegalStateException) {
            startKoin {
                baseApiRetrofit = MockedBaseApi()
                val serviceTestModules = module {
                    single { baseApiRetrofit as IBaseApi }
                    single { NewsApi(get()) }
                    single { TopHeadlineRepository(get()) as ITopHeadlineRepository }
                }
                val appTestComponent = listOf(
                    viewModelModules,
                    useCaseModules,
                    serviceTestModules,
                    mapperModules
                )
                modules(appTestComponent)
            }
        }
    }

    //check if the test is Instrumented or Unit, in Unit tests we set RxJava to run synchronously
    fun init() {
        initKoin()
    }

    fun clear() {
        clearEndpointMocks()
        stopKoin()
    }

    fun returnJsonData(path: String): String {
        return FileUtils.readJson(path.substring(1) + ".json")!!
    }
}