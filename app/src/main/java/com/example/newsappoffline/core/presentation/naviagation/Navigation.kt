package com.example.newsappoffline.core.presentation.naviagation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.newsappoffline.article.ArticleDetailsScreen
import com.example.newsappoffline.news.presentation.NewsScreen

@Composable
fun Navigation(
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(navController = navHostController, startDestination = News) {
        composable<News> {
            NewsScreen { id->
                navHostController.navigate(ArticleScreen(articleId = id))
            }
        }

        composable<ArticleScreen> {navBackStackEntry ->
            val args = navBackStackEntry.toRoute<ArticleScreen>()
            ArticleDetailsScreen(
                articleId = args.articleId
            )
        }
    }
    
}