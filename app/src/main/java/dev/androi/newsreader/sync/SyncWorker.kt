package dev.androi.newsreader.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.androi.newsreader.data.remote.NetworkModule
import dev.androi.newsreader.data.local.FeedRepository

class SyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            val repo = FeedRepository(NetworkModule.feedApi, applicationContext)
            repo.refreshAndCache()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}