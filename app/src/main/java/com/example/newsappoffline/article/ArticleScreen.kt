package com.example.newsappoffline.article

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.newsappoffline.core.domain.model.Article

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailsScreen(
    articleViewModel: ArticleViewModel = hiltViewModel(),
    articleId: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigate:()->Unit
) {
    LaunchedEffect(articleId) {
        Log.d(TAG, "ArticleDetailsScreen: $articleId")
        articleViewModel.onAction(ArticleAction.LoadArticle(articleId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(start = 5.dp),
                title = {
                    Text(
                        text = "Back"
                    )
                },
                navigationIcon ={
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 4.dp)
                            .clickable {
                                onNavigate()
                            }
                    )

                },
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (articleViewModel.articleState.isLoading && articleViewModel.articleState.article == null) {
                CircularProgressIndicator()
            }

            if (articleViewModel.articleState.error && articleViewModel.articleState.article == null) {
                Text(
                    text = "Can't Load Data ${articleViewModel.articleState.error}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        articleViewModel.articleState.article?.let {
            innerArticleScreen(
                article = it,
                modifier = Modifier.padding(padding),
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun innerArticleScreen(
    article: Article,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {

    with(sharedTransitionScope) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.background)
                .padding(vertical = 16.dp)
                .padding(top = 8.dp)
        ) {
            Text(
                text = article.source_name,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.Serif,
                maxLines = 3,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(15.dp)
                    .sharedElement(
                        state = sharedTransitionScope.rememberSharedContentState("text"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = article.pubDate,
                style = MaterialTheme.typography.titleSmall,
                fontFamily = FontFamily.Serif,
                maxLines = 3,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(15.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(15.dp)
            )
            Spacer(Modifier.height(16.dp))

            AsyncImage(
                model = article.image_url,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .sharedElement(
                        state = sharedTransitionScope.rememberSharedContentState("bounds"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = article.description,
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            Text(
                text = article.content,
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }

}
