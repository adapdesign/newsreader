package dev.androi.newsreader.data.remote

import retrofit2.http.GET

interface FeedApi {
    @GET("/api/getFeeds")
    suspend fun getFeeds(): FeedResponse
}
