package com.example.newsappoffline.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [ArticleEntity::class], version = 2, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {
    abstract val  articleDao:ArticleDao
}