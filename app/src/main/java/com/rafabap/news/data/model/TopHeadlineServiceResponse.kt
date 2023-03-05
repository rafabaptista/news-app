package com.rafabap.news.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class TopHeadlineServiceResponse(
    val articles: List<ArticleResponse>? = null
) : Serializable