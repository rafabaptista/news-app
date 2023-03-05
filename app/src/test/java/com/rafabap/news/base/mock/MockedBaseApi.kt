package com.rafabap.news.base.mock

import android.util.Log
import com.google.gson.GsonBuilder
import com.rafabap.news.base.mock.endpoint.EndpointMock
import com.rafabap.news.data.network.api.IBaseApi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class MockedBaseApi : IBaseApi {

    private val jsonMediaType = "application/json".toMediaTypeOrNull()
    private val mockedEndpoints = HashMap<String, EndpointMock>()

    override fun build(): Retrofit {
        val baseUrl = BASE_MOCKED_URL
        val timeout = 3

        val builder = getBuilder()

        val clientBuilder = builder
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(timeout.toLong(), TimeUnit.SECONDS)

        val client = clientBuilder.build()
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )

        return retrofitBuilder.build()
    }

    override fun getBuilder() = OkHttpClient.Builder()
        .addInterceptor(mockInterceptor())

    private fun mockInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val path = getPath(request)
            val endpoint = request.method.uppercase(Locale.getDefault()) + " " + path
            var mock: EndpointMock? = mockedEndpoints[endpoint]
            if (mock == null) {
                mock = mockedEndpoints[path]
            }
            if (mock != null) {
                return@Interceptor customResponse(chain, mock)
            }

            defaultResponse(chain, path)
        }
    }

    private fun getPath(request: Request): String {
        var path = request.url.encodedPath
        if ("/" == path) {
            path = request.url.toString()
        }
        return path
    }

    //custom responses are dynamic mocks, you can create it in the test code
    private fun customResponse(chain: Interceptor.Chain, mock: EndpointMock): Response {
        val builder = defaultBuilder(chain)

        if (mock.getCode() == EndpointMock.FORCED_MOCK_EXCEPTION_CODE) {
            return builder.code(500)
                .body("{\"status\": \"com.rafabap.news.presentation.util.extension.error\"}".toResponseBody(jsonMediaType))
                .build()
        }
        return mock.error?.let { error ->
            builder.code(mock.getCode())
                .body(error.response()!!.errorBody())
                .build()
        } ?: builder.code(mock.getCode())
            .body(mock.getResponse(chain.request()).toResponseBody(jsonMediaType))
            .build()
    }

    //default response is getting the response from JSON files
    private fun defaultResponse(chain: Interceptor.Chain, endpoint: String): Response {
        val builder = defaultBuilder(chain)
        val content = FileUtils.readJson(endpoint.substring(1) + ".json")
            ?: return endpointNotMocked(endpoint)
        return builder.code(200)
            .body(content.toResponseBody(jsonMediaType))
            .build()
    }

    private fun defaultBuilder(chain: Interceptor.Chain): Response.Builder {
        val builder = Response.Builder()
        return builder.message("")
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
    }

    private fun endpointNotMocked(endpoint: String): Response {
        Log.e("MockedApiRetrofit", "endpoint not mocked -> $endpoint")
        throw RuntimeException("endpoint not mocked -> $endpoint")
    }

    fun addMockedEndpoint(url: String, mock: EndpointMock) {
        mockedEndpoints[url] = mock
    }

    fun clearMocks() {
        mockedEndpoints.clear()
    }

    companion object {
        private const val BASE_MOCKED_URL = "http://MOCKED.net"
    }
}