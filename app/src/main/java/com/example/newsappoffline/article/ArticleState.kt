package com.example.newsappoffline.article

import com.example.newsappoffline.core.domain.model.Article

data class ArticleState(
    val article : Article? =null,
    val isLoading : Boolean = false,
    val error : Boolean = false
)
