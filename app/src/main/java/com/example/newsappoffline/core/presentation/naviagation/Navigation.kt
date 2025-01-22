package com.example.newsappoffline.core.presentation.naviagation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.newsappoffline.article.ArticleDetailsScreen
import com.example.newsappoffline.news.presentation.NewsScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation(
    navHostController: NavHostController = rememberNavController()
) {
    SharedTransitionLayout {

            NavHost(navController = navHostController, startDestination = News) {
                composable<News> {
                    NewsScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                        onItemClick = {
                            navHostController.navigate(ArticleScreen(articleId = it))
                        }
                    )
                }

                composable<ArticleScreen> { navBackStackEntry ->
                    val args = navBackStackEntry.toRoute<ArticleScreen>()
                    ArticleDetailsScreen(
                        articleId = args.articleId,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                        onNavigate = { navHostController.popBackStack()}
                    )
                }
            }
        }


}