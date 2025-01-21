package com.example.newsappoffline.core.data.model

data class NewsListDto(
    val nextPage: String? = "",
    val results: List<ArticleDto>? = listOf(),
)