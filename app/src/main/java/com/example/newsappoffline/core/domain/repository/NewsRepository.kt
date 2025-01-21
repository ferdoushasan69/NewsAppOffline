package com.example.newsappoffline.core.domain.repository

import com.example.newsappoffline.core.domain.model.Article
import com.example.newsappoffline.core.domain.model.NewsList
import com.example.newsappoffline.utils.NewsResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getAllNews(): Flow<NewsResponse<NewsList>>
    suspend fun paginate(nextPage: String?): Flow<NewsResponse<NewsList>>

    suspend fun getArticle(articleId: String): Flow<NewsResponse<Article>>
}