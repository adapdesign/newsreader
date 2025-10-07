package dev.androi.newsreader.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeedResponse(val items: List<NetworkArticle> = emptyList())

@JsonClass(generateAdapter = true)
data class NetworkArticle(
    val id: String,
    val title: String,
    val link: String? = "",
    val publishedAt: String? = "",
    val summary: String? = "",
    val source: String? = "",
    val image: String? = ""
)