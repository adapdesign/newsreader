package dev.androi.newsreader.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ArticleDetailViewModelFactory(
    private val appContext: Context,
    private val articleId: String,
    private val articleUrl: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticleDetailViewModel(appContext, articleId, articleUrl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}