package com.rafabap.news.base.mock

/**
 * Each URL must match the API uri from INewsApiRestService Retrofit interface;
 * Also, the Json file must be created with the same name.
 * Example:
 *      Retrofit -> @GET("top-headlines") | const -> "/top-headlines" | Json file -> top-headlines.json
 * **/
object TestConstants {
    const val TOP_HEADLINES_URL = "/top-headlines"
}