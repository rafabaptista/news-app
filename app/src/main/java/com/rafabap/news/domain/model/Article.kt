package com.rafabap.news.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Article (
    val sourceId: String,
    val sourceName: String,
    val author: String,
    val title: String,
    val hasDescription: Boolean,
    val description: String,
    val url: String,
    val hasImageUrl: Boolean,
    val urlToImage: String,
    val publishedAt: String,
    val hasContent: Boolean,
    val content: String
): java.io.Serializable {

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        var result = sourceId.hashCode()
        result = HAS_CODE_MULTIPLIER * result + sourceName.hashCode()
        result = HAS_CODE_MULTIPLIER * result + author.hashCode()
        result = HAS_CODE_MULTIPLIER * result + title.hashCode()
        result = HAS_CODE_MULTIPLIER * result + hasDescription.hashCode()
        result = HAS_CODE_MULTIPLIER * result + description.hashCode()
        result = HAS_CODE_MULTIPLIER * result + url.hashCode()
        result = HAS_CODE_MULTIPLIER * result + hasImageUrl.hashCode()
        result = HAS_CODE_MULTIPLIER * result + urlToImage.hashCode()
        result = HAS_CODE_MULTIPLIER * result + publishedAt.hashCode()
        result = HAS_CODE_MULTIPLIER * result + hasContent.hashCode()
        result = HAS_CODE_MULTIPLIER * result + content.hashCode()
        return result
    }

    private companion object {
        const val HAS_CODE_MULTIPLIER = 31
    }
}