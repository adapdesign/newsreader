package dev.androi.newsreader.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.androi.newsreader.data.remote.NetworkModule
import dev.androi.newsreader.data.local.ArticleEntity
import dev.androi.newsreader.data.local.FeedRepository
import dev.androi.newsreader.data.utils.addDefaultHttpsScheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ArticleDetailViewModel(context: Context, private val articleId: String?, private val articleUrl: String?) : ViewModel() {
    private val repo = FeedRepository(NetworkModule.feedApi, context)
    private val _articleFlow = MutableStateFlow<ArticleEntity?>(null)
    val articleFlow: StateFlow<ArticleEntity?> = _articleFlow.asStateFlow()
    var loading by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            if (!articleId.isNullOrEmpty()) {
                repo.observeArticle(articleId).collect { _articleFlow.value = it }
//                if (_articleFlow.value == null && !articleUrl.isNullOrEmpty()) loadRemoteToCache(articleId, articleUrl)
            } else if (!articleUrl.isNullOrEmpty()) {
                _articleFlow.value = repo.getByLink(articleUrl)
            }
        }
    }

    private suspend fun loadRemoteToCache(id: String?, url: String) {
        loading = true
        try {
//            val html = fetchHtml(url.addDefaultHttpsScheme())
//            if (id != null) repo.saveArticleContent(id, html) else {
//                val genId = url
//                repo.saveArticleContent(genId, html)
//                _articleFlow.value = repo.getById(genId)
//            }
        } finally { loading = false }
    }

    fun toggleSave() {
        viewModelScope.launch {
            _articleFlow.value?.let {
                if (it.content.isNullOrEmpty() && it.link != null) {
                    loadRemoteToCache(
                        id = it.id,
                        url = it.link.addDefaultHttpsScheme()
                    )
                }
            }
        }
    }

    fun markRead() {
        viewModelScope.launch {
            _articleFlow.value?.id?.let {
                repo.setRead(it, true);
                _articleFlow.value = _articleFlow.value?.copy(isRead = true)
            }
        }
    }

    fun unmarkRead() {
        viewModelScope.launch {
            _articleFlow.value?.id?.let {
                repo.setRead(it, false);
                _articleFlow.value = _articleFlow.value?.copy(isRead = false)
            }
        }
    }
    private suspend fun fetchHtml(url: String): String {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val req = Request.Builder().url(url).build()
            client.newCall(req).execute().use {
                resp -> resp.body?.string() ?: ""
            }
        }
    }
}
