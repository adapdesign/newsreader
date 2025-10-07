package dev.androi.newsreader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.androi.newsreader.ui.screens.ArticleDetailScreen
import dev.androi.newsreader.ui.screens.ArticlesScreen

object Destinations {
    const val ARTICLES = "articles"
    const val ARTICLE_DETAILS = "articleDetail"
    fun articleDetailRoute(articleId: String, encodedUrl: String) = "$ARTICLE_DETAILS/$articleId/$encodedUrl"
}

@Composable
fun NewsReaderNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destinations.ARTICLES) {
        composable(Destinations.ARTICLES) {
            ArticlesScreen(onOpenArticle = { id, url  ->
                val encodeId = java.net.URLEncoder.encode(id, "UTF-8")
                val encodedUrl = java.net.URLEncoder.encode(url, "UTF-8")
                navController.navigate(Destinations.articleDetailRoute(encodeId, encodedUrl))
            })
        }
        composable(
            route = "${Destinations.ARTICLE_DETAILS}/{article}/{url}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType; defaultValue = "" },
                navArgument("articleId") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
            val articleUrl = java.net.URLDecoder.decode(encodedUrl, "UTF-8")
            val encodedId = backStackEntry.arguments?.getString("article") ?: ""
            val articleId = java.net.URLDecoder.decode(encodedId, "UTF-8")
            ArticleDetailScreen(
                articleId = articleId,
                articleUrl = articleUrl,
                onBack = { navController.popBackStack() })
        }
    }

}