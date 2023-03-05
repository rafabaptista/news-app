package com.rafabap.news.base.mock.endpoint

import com.rafabap.news.base.mock.MockedBaseApi
import okhttp3.Request
import retrofit2.HttpException

/** Mock URL **/
class EndpointMock(private val url: String, private val mockedBaseApi: MockedBaseApi?) {
    private var responseHandler: ResponseHandler? = null
    private var responseHandlerFun: ((request: Request, path: String) -> String)? = null

    private var response: String? = null
    private var code = 200
    var error: HttpException? = null
        private set
    private var method: String? = null

    fun getCode(): Int {
        return code
    }

    fun getResponse(request: Request): String {
        if (responseHandlerFun != null) {
            return responseHandlerFun!!.invoke(request, url)
        } else if (responseHandler != null) {
            return responseHandler!!.getResponse(request, url)
        }
        return response ?: ""
    }

    /** Force com.rafabap.news.presentation.util.extension.error **/
    fun throwConnectionError(): EndpointMock {
        this.code = FORCED_MOCK_EXCEPTION_CODE
        return this
    }

    /** The response code this mock should send **/
    fun code(code: Int): EndpointMock {
        this.code = code
        return this
    }

    /** A handler to mock dynamically with multiple cases **/
    fun body(handler: ResponseHandler): EndpointMock {
        this.responseHandler = handler
        return this
    }

    /** Set the EndpointMock and make it active **/
    fun apply() {
        mockedBaseApi?.let {
            var path = url
            if (method != null) {
                path = "$method $path"
            }
            mockedBaseApi.addMockedEndpoint(path, this)
        } ?: throw RuntimeException("EndpointService not mocked!")

    }

    companion object {
        const val FORCED_MOCK_EXCEPTION_CODE = 999
    }
}