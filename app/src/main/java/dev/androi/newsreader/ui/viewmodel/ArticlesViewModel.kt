package dev.androi.newsreader.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.androi.newsreader.data.local.ArticleEntity
import dev.androi.newsreader.data.local.FeedRepository
import dev.androi.newsreader.data.remote.NetworkModule
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ArticlesViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = FeedRepository(NetworkModule.feedApi, app.applicationContext)
    private val cached = repo.observeCachedArticles()

    // Handle Search Query
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()
    fun setQuery(q: String) {
        _query.value = q
    }

    // combined filtered UI state
    @OptIn(FlowPreview::class)
    val uiState: StateFlow<List<ArticleEntity>> = combine(cached, _query.debounce { 150 }) { list, q ->
        val trimmed = q.trim()
        if (trimmed.isEmpty()) {
            list
        } else {
            val lower = trimmed.lowercase()
            list.filter { entity ->
                entity.title.lowercase().contains(lower) ||
                    (entity.summary?.lowercase()?.contains(lower) == true) ||
                    (entity.link?.lowercase()?.contains(lower) == true)
            }
        }
    }
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            repo.refreshAndCache()
        }
    }
}
