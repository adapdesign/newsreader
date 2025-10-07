package dev.androi.newsreader

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import dev.androi.newsreader.sync.SyncWorker
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class NewsReaderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val work = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "news_sync",
            ExistingPeriodicWorkPolicy.REPLACE,
            work
        )
    }
}