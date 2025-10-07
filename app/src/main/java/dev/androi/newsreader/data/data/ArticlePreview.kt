package dev.androi.newsreader.data.data

data class ArticlePreview(
    val id: String,
    val title: String,
    val source: String,
    val summary: String,
    val image: String,
    val isRead: Boolean
)