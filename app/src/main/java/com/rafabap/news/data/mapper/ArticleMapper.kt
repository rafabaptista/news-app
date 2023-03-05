package com.rafabap.news.data.mapper

import com.rafabap.news.data.model.ArticleResponse
import com.rafabap.news.domain.model.Article

class ArticleMapper {
    fun map(articleResponse: List<ArticleResponse>) = articleResponse.map { map(it) }

    fun map(articleResponse: ArticleResponse) = Article(
            sourceId = articleResponse.source?.id ?: "",
            sourceName = articleResponse.source?.name ?: "",
            author = articleResponse.author ?: "",
            title = articleResponse.title ?: "",
            hasDescription = !articleResponse.description.isNullOrEmpty(),
            description = articleResponse.description ?: "",
            url = articleResponse.url ?: "",
            hasImageUrl = !articleResponse.urlToImage.isNullOrEmpty(),
            urlToImage = articleResponse.urlToImage ?: "",
            publishedAt = articleResponse.publishedAt ?: "",
            hasContent = !articleResponse.content.isNullOrEmpty(),
            content = articleResponse.content ?: ""
        )
}