package dev.androi.newsreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.androi.newsreader.ui.navigation.NewsReaderNavHost
import dev.androi.newsreader.ui.theme.NewsReaderTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsReaderTheme {
                NewsReaderNavHost()
            }
        }
    }
}