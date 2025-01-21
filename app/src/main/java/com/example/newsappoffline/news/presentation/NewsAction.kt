package com.example.newsappoffline.news.presentation

sealed interface NewsAction {
    data object Paginate:NewsAction
}