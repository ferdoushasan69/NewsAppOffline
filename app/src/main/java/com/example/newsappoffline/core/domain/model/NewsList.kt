package com.example.newsappoffline.core.domain.model

data class NewsList(
    val nextPage: String,
    val results: List<Article>,
)