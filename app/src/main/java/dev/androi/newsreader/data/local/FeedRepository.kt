package dev.androi.newsreader.data.local

import android.content.Context
import android.util.Log
import dev.androi.newsreader.data.remote.FeedApi
import kotlinx.coroutines.flow.Flow

class FeedRepository(private val api: FeedApi, val context: Context) {
    private val dao = AppDatabase.get(context).articleDao()

    fun observeCachedArticles(): Flow<List<ArticleEntity>> =
        dao.observeAll()

    suspend fun refreshAndCache() {
        Log.d("Fetch", "Remote Fetch Now")
        val remoteList = try { api.getFeeds().items } catch (e: Exception) { emptyList() }
        val entities = remoteList.map { remote ->
            val local = dao.getById(remote.id)
            ArticleEntity(
                id = remote.id,
                title = remote.title,
                link = remote.link,
                publishedAt = remote.publishedAt,
                summary = remote.summary,
                source = remote.source,
                image = remote.image,
                content = local?.content ?: "",
                isRead = local?.isRead ?: false,
                fetchedAt = System.currentTimeMillis()
            )
        }
        if (entities.isNotEmpty()) {
            dao.upsertAll(entities)
            // purge older than 14 days
            val minTs = System.currentTimeMillis() - 14L * 24 * 60 * 60 * 1000
            dao.deleteOlderThan(minTs)
        }
    }

    suspend fun getById(id: String) = AppDatabase.get(context).articleDao().getById(id)
    suspend fun getByLink(url: String) = AppDatabase.get(context).articleDao().getByLink(url)
    suspend fun setRead(id: String, read: Boolean) = AppDatabase.get(context).articleDao().setRead(id, read)

    fun observeArticle(id: String) = AppDatabase.get(context).articleDao().observeById(id)


    suspend fun saveArticleContent(id: String, contentHtml: String) {
        val dao = AppDatabase.get(context).articleDao()
        val existing = dao.getById(id)
        val entity = existing?.copy(content = contentHtml) ?: ArticleEntity(
            id = id,
            title = "Saved",
            link = null,
            publishedAt = null,
            summary = null,
            source = null,
            image = null,
            content = contentHtml,
            fetchedAt = System.currentTimeMillis(),
            isRead = false
        )
        dao.upsert(entity)
    }

}