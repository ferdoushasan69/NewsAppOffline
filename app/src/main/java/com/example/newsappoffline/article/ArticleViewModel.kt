package com.example.newsappoffline.article

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsappoffline.core.domain.repository.NewsRepository
import com.example.newsappoffline.utils.NewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var articleState by mutableStateOf(ArticleState())
        private set

    fun onAction(articleAction: ArticleAction) {
        when (articleAction) {
            is ArticleAction.LoadArticle -> {
                getArticle(
                    articleId = articleAction.articleId
                )
            }
        }
    }

    private fun getArticle(articleId: String) {
        if (articleId.isEmpty()) {
            articleState = articleState.copy(error = true)
            Log.d(TAG, "getArticle: ${articleState.article?.article_id}")
            return
        }
        viewModelScope.launch {
            articleState = articleState.copy(isLoading = true)

            newsRepository.getArticle(articleId).collect { result ->
                articleState = when (result) {
                    is NewsResponse.Success -> {
                        articleState.copy(article = result.data)
                    }

                    is NewsResponse.Error -> {
                        articleState.copy(error = true, isLoading = false)
                    }

                    is NewsResponse.Loading -> {
                        articleState.copy(isLoading = true)
                    }
                }
            }
            articleState = articleState.copy(isLoading = false)
        }
    }
}