package com.example.newsappoffline.news.presentation

import com.example.newsappoffline.core.domain.model.Article

data class NewsState(
    val article : List<Article> = emptyList(),
    val nextPage : String? = null,
    val isLoading : Boolean = false,
    val error : Boolean = false,

)
