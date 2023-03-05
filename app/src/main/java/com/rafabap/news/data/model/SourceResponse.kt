package com.rafabap.news.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SourceResponse(
    val id: String? = null,
    val name: String? = null
) : Serializable