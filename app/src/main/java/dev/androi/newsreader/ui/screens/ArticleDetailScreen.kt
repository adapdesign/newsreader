package dev.androi.newsreader.ui.screens

import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.androi.newsreader.data.data.ArticleDetail
import dev.androi.newsreader.data.data.toDetail
import dev.androi.newsreader.ui.viewmodel.ArticleDetailViewModel
import dev.androi.newsreader.ui.viewmodel.ArticleDetailViewModelFactory
import dev.androi.newsreader.ui.components.AppBarTitle
import dev.androi.newsreader.ui.components.BackIcon
import dev.androi.newsreader.ui.components.DownloadIcon
import dev.androi.newsreader.ui.components.FullScreenLoading
import dev.androi.newsreader.ui.components.ReadIcon
import dev.androi.newsreader.ui.components.ShareIcon
import dev.androi.newsreader.ui.components.UnreadIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(articleId: String, articleUrl: String, onBack: () -> Unit = {}) {
    val ctx = LocalContext.current
    val vm: ArticleDetailViewModel = viewModel(factory = ArticleDetailViewModelFactory(
        ctx.applicationContext,
        articleId,
        articleUrl
    ))
    val articleEntity by vm.articleFlow.collectAsState(initial = null)
    val article = articleEntity?.toDetail()

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(Modifier.systemBarsPadding().fillMaxSize()) {
            TopAppBar(title = { Text("") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    BackIcon()
                }
            }, actions = {
                IconButton (onClick = {
                    vm.toggleSave()
                    Toast.makeText(ctx, "Article downloaded for offline use", Toast.LENGTH_SHORT).show()
                }) {
                    DownloadIcon()
                }
                if (article?.isRead == true) {
                    IconButton(onClick = {
                        vm.unmarkRead()
                        Toast.makeText(ctx, "Mark as unread", Toast.LENGTH_SHORT).show()
                    }) {
                        UnreadIcon()
                    }
                } else {
                    IconButton(onClick = {
                        vm.markRead()
                        Toast.makeText(ctx, "Mark as read", Toast.LENGTH_SHORT).show()
                    }) {
                        ReadIcon()
                    }
                }
                IconButton(onClick = {
                    article?.id?.let { url ->
                        val i = Intent(Intent.ACTION_VIEW, url.toUri()); ctx.startActivity(i)
                    }
                }) {
                    ShareIcon()
                }
            })

            Box(Modifier.padding(16.dp)) {
                AppBarTitle(title = article?.title ?: "Article Title")
            }

            Box(Modifier.weight(1f)) {
                when {
                    vm.loading -> FullScreenLoading()
                    !article?.content.isNullOrEmpty() -> ArticleLocalWebView(article)
                    article?.link != null -> ArticleRemoteWebView(article)
                    else -> Text("No content available", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun ArticleLocalWebView(article: ArticleDetail?) {
    AndroidView(factory = { ctx ->
        WebView(ctx).apply {
            settings.javaScriptEnabled = false
            loadDataWithBaseURL(article?.link, article?.content ?: "", "text/html", "utf-8", null)
        }
    }, modifier = Modifier.fillMaxSize())
}

@Composable
fun ArticleRemoteWebView(article: ArticleDetail?) {
    article?.let {
        AndroidView(factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(it.link)
            }
        }, modifier = Modifier.fillMaxSize())
    }
}