package com.rafabap.news.domain.model

class Photo(
    val id: String,
    val normalPhotoUrl: String,
    val largePhotoUrl: String
) {
    override fun equals(other: Any?): Boolean {
        return this === other //super.equals(other)
    }
}