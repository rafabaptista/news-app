package com.rafabap.news.base.mock.endpoint

import okhttp3.Request

interface ResponseHandler {
    fun getResponse(request: Request, path: String): String
}