package com.example.newsappoffline.article

sealed interface ArticleAction {
    data class LoadArticle(val articleId : String) : ArticleAction
}