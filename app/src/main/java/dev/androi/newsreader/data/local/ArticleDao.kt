package dev.androi.newsreader.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun observeAll(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<ArticleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ArticleEntity)

    @Query("UPDATE articles SET is_read = :read WHERE id = :id")
    suspend fun setRead(id: String, read: Boolean)

    @Query("DELETE FROM articles WHERE fetched_at < :minTs")
    suspend fun deleteOlderThan(minTs: Long)

    @Query("SELECT * FROM articles WHERE link = :url LIMIT 1")
    suspend fun getByLink(url: String): ArticleEntity?

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ArticleEntity?
}