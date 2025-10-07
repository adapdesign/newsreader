package dev.androi.newsreader.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.androi.newsreader.R

@Composable
fun DownloadIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_download),
        tint = MaterialTheme.colorScheme.onBackground,
        contentDescription = "Download",
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun ReadIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_read),
        tint = MaterialTheme.colorScheme.onBackground,
        contentDescription = "Read",
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun ReadIcon(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.ic_read),
        tint = MaterialTheme.colorScheme.onBackground,
        contentDescription = "Read",
        modifier = modifier.size(24.dp)
    )
}

@Composable
fun UnreadIcon() {
    Icon(
        painter = painterResource(id = R.drawable.ic_unread),
        tint = MaterialTheme.colorScheme.onBackground,
        contentDescription = "Unread",
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun UnreadIcon(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.ic_unread),
        tint = MaterialTheme.colorScheme.onBackground,
        contentDescription = "Unread",
        modifier = modifier.size(24.dp)
    )
}

@Composable
fun BackIcon() {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "Back"
    )
}

@Composable
fun ShareIcon() {
    Icon(
        imageVector = Icons.Default.Share,
        contentDescription = "Open in browser"
    )
}

@Composable
fun SearchIcon() {
    Icon(
        Icons.Default.Search, contentDescription = null
    )
}
