package com.example.newsappoffline.core.domain.model

data class Article(
    val article_id: String,
    val content: String,
    val description: String,
    val image_url: String,
    val language: String,
    val pubDate: String,
    val source_icon:String,
    val source_id: String,
    val source_name: String,
    val source_url: String,
    val title: String
)