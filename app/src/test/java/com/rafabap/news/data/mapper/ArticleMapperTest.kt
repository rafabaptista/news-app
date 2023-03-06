package com.rafabap.news.data.mapper

import com.rafabap.news.data.model.ArticleResponse
import com.rafabap.news.data.model.SourceResponse
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ArticleMapperTest {

    private lateinit var mapper: ArticleMapper

    private val data = ArticleResponse(
        source = SourceResponse("id", "name"),
        author = "author",
        title = "title",
        description = "description",
        url = "url",
        urlToImage = "urlToImage",
        publishedAt = "publishedAt",
        content = "content"
    )
    private val data2 = ArticleResponse(
        source = SourceResponse("id2", "name2"),
        author = "author2",
        title = "title2",
        description = "description2",
        url = "url2",
        urlToImage = null,
        publishedAt = "publishedAt2",
        content = "content2"
    )

    private val data3 = ArticleResponse(
        source = SourceResponse("id3", "name3"),
        author = "author3",
        title = "title3",
        description = null,
        url = "url3",
        urlToImage = null,
        publishedAt = "publishedAt3",
        content = null
    )

    @Before
    fun setupTest() {
        mapper = ArticleMapper()
    }

    @Test
    fun `test that single map article is working successfully`() {
        val testSingleMap = mapper.map(data)

        assertEquals("id", testSingleMap.sourceId)
        assertEquals("name", testSingleMap.sourceName)
        assertEquals("title", testSingleMap.title)
        assertEquals(true, testSingleMap.hasDescription)
        assertEquals("description", testSingleMap.description)
        assertEquals("url", testSingleMap.url)
        assertEquals(true, testSingleMap.hasImageUrl)
        assertEquals("urlToImage", testSingleMap.urlToImage)
        assertEquals("publishedAt", testSingleMap.publishedAt)
        assertEquals(true, testSingleMap.hasContent)
        assertEquals("content", testSingleMap.content)
    }

    @Test
    fun `test that list map article is working successfully`() {
        val testListMap = mapper.map(listOf(data, data2, data3))

        val testFirstMap = testListMap.first()
        assertEquals("id", testFirstMap.sourceId)
        assertEquals("name", testFirstMap.sourceName)
        assertEquals("title", testFirstMap.title)
        assertEquals(true, testFirstMap.hasDescription)
        assertEquals("description", testFirstMap.description)
        assertEquals("url", testFirstMap.url)
        assertEquals(true, testFirstMap.hasImageUrl)
        assertEquals("urlToImage", testFirstMap.urlToImage)
        assertEquals("publishedAt", testFirstMap.publishedAt)
        assertEquals(true, testFirstMap.hasContent)
        assertEquals("content", testFirstMap.content)

        val testSecondMap = testListMap[1]
        assertEquals("id2", testSecondMap.sourceId)
        assertEquals("name2", testSecondMap.sourceName)
        assertEquals("title2", testSecondMap.title)
        assertEquals(true, testSecondMap.hasDescription)
        assertEquals("description2", testSecondMap.description)
        assertEquals("url2", testSecondMap.url)
        assertEquals(false, testSecondMap.hasImageUrl)
        assertEquals("", testSecondMap.urlToImage)
        assertEquals("publishedAt2", testSecondMap.publishedAt)
        assertEquals(true, testSecondMap.hasContent)
        assertEquals("content2", testSecondMap.content)

        val testThirdMap = testListMap.last()
        assertEquals("id3", testThirdMap.sourceId)
        assertEquals("name3", testThirdMap.sourceName)
        assertEquals("title3", testThirdMap.title)
        assertEquals(false, testThirdMap.hasDescription)
        assertEquals("", testThirdMap.description)
        assertEquals("url3", testThirdMap.url)
        assertEquals(false, testThirdMap.hasImageUrl)
        assertEquals("", testThirdMap.urlToImage)
        assertEquals("publishedAt3", testThirdMap.publishedAt)
        assertEquals(false, testThirdMap.hasContent)
        assertEquals("", testThirdMap.content)
    }
}