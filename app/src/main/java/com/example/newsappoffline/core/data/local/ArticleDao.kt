package com.example.newsappoffline.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import retrofit2.http.DELETE

@Dao
interface ArticleDao {

    @Upsert
    suspend fun upsertArticleList(articleList : List<ArticleEntity>)

    @Query("SELECT * FROM articleentity")
    suspend fun getArticleList() : List<ArticleEntity>

    @Query("SELECT * FROM articleentity WHERE  article_id= :articleId")
    suspend fun getArticleId(articleId : String) : ArticleEntity?

    @Query("DELETE FROM articleentity")
    suspend fun clearDatabase()
}