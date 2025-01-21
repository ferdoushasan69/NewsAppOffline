package com.example.newsappoffline.news.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.util.TableInfo
import coil3.compose.AsyncImage
import com.example.newsappoffline.core.domain.model.Article
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,

    ) {
    NewsScreen(
        state = newsViewModel.state,
        onItemClick = onItemClick,
        onAction = newsViewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsScreen(
    state: NewsState,
    onItemClick: (String) -> Unit,
    onAction: (NewsAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = { Text(text = "Daily News") },
                windowInsets = WindowInsets(top = 50.dp, bottom = 8.dp)
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading && state.article.isEmpty()) {
                CircularProgressIndicator()
            }

            if (state.error && state.article.isEmpty()) {
                Text(
                    text = "Can't Load Data ",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Red
                )
            }

            if (state.article.isNotEmpty()) {
                val listState = rememberLazyListState()

                val shouldPaginate = remember {
                    derivedStateOf {
                        val totalItem = listState.layoutInfo.totalItemsCount
                        val lastItemVisible =
                            listState.layoutInfo.visibleItemsInfo.lastOrNull() ?: 0
                        lastItemVisible == totalItem - 1 && !state.isLoading && state.nextPage != null
                    }
                }

                Log.d(TAG, "shouldPaginate: ${shouldPaginate.value}")

                LaunchedEffect(key1 = listState) {
                    Log.d(TAG, "NewsScreen: ${state.nextPage}")
                    snapshotFlow { shouldPaginate.value }
                        .distinctUntilChanged()
                        .filter { it }
                        .collect {
                            Log.d(TAG, "paginate triggered : ${state.nextPage} ")
                            onAction(NewsAction.Paginate) }

                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    state = listState
                ) {
                    itemsIndexed(
                        items = state.article,
                        key = { _, article -> article.article_id }
                    ) { index, item ->
                        ArticleItem(
                            article = item,
                            onArticleClick =onItemClick
                        )
                    }
                }

            }
        }

    }


}

@Composable
fun ArticleItem(
    article: Article,
    onArticleClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable {
                onArticleClick(article.article_id)
                Log.d(TAG, "ArticleItem click: ${article.article_id}")
            },
    ) {
        Text(
            text = article.source_name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = article.title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        AsyncImage(
            model = article.image_url,
            contentScale = ContentScale.Crop,
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .height(250.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = article.description,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = article.pubDate,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(color = MaterialTheme.colorScheme.surfaceVariant.copy(0.7f))
    )
}
