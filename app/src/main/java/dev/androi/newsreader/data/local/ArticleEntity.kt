package dev.androi.newsreader.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val link: String? = "",
    val publishedAt: String? = "",
    val summary: String? = "",
    val source: String? = "",
    val image: String? = "",
    @ColumnInfo(name = "content") val content: String? = null,
    @ColumnInfo(name = "is_read") val isRead: Boolean = false,
    @ColumnInfo(name = "fetched_at") val fetchedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "views") val views: Int = 0,
    @ColumnInfo(name = "liked") val liked: Boolean = false,
)