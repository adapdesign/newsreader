package dev.androi.newsreader.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.androi.newsreader.R
import dev.androi.newsreader.data.data.ArticlePreview
import dev.androi.newsreader.data.data.toPreview
import dev.androi.newsreader.data.utils.addDefaultHttpsScheme
import dev.androi.newsreader.ui.components.ReadIcon
import dev.androi.newsreader.ui.components.SearchIcon
import dev.androi.newsreader.ui.viewmodel.ArticlesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesScreen(onOpenArticle: (String, String) -> Unit) {
    val vm: ArticlesViewModel = viewModel()
    val articles by vm.uiState.collectAsState()
    val ctx = LocalContext.current
    val query by vm.query.collectAsState()
    var searchVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(searchVisible) {
        if (searchVisible) {
            focusRequester.requestFocus()
        } else {
            focusManager.clearFocus()
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(Modifier.systemBarsPadding().fillMaxSize().padding(16.dp)) {
            TopAppBar(title = { Text(ctx.getString(R.string.app_name)) }, actions = {
                IconButton(onClick = {searchVisible = !searchVisible}) {
                    SearchIcon()
                }
            })

            AnimatedVisibility(
                visible = searchVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = vm::setQuery,
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                    leadingIcon = { SearchIcon() },
                    placeholder = { Text("Search") },
                    singleLine = true

                )
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(articles) { article ->
                    ArticleRow(
                        article = article.toPreview(),
                        onClick = { onOpenArticle(article.id, article.link ?: "") }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleRow(article: ArticlePreview, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp)
        .clickable { onClick() }) {
        Row(modifier = Modifier.padding(12.dp)) {
            if (article.image.isNotEmpty()) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(article.image.addDefaultHttpsScheme())
                            .crossfade(true)
                            .build(),
                        contentDescription = article.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight()
                            .size(120.dp)
                            .padding(all = 6.dp)
                            .alpha( if (article.isRead) 0.35f else 1f )
                            .clip(RoundedCornerShape(6.dp))
                    )
                    if (article.isRead) {
                        ReadIcon(modifier = Modifier.align(Alignment.Center).padding(6.dp))
                    }
                }

            }
            Column(modifier = Modifier.padding(12.dp).alpha( if (article.isRead) 0.35f else 1f )) {
                Text(text = article.title, style = MaterialTheme.typography.titleMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "${article.source} • ${article.summary}", maxLines = 2, style = MaterialTheme.typography.bodyMedium, overflow = TextOverflow.Ellipsis)
            }
        }

    }
}