package com.example.newsappoffline.core.presentation.naviagation

import kotlinx.serialization.Serializable

@Serializable
data object News

@Serializable
data class ArticleScreen(val articleId : String)