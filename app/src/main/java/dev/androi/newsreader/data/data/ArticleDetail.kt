package dev.androi.newsreader.data.data

data class ArticleDetail(
    val id: String,
    val title: String,
    val link: String,
    val isRead: Boolean,
    val content: String
)