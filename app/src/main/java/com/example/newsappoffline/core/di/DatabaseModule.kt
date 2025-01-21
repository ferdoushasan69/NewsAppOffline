package com.example.newsappoffline.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newsappoffline.core.data.local.ArticleDao
import com.example.newsappoffline.core.data.local.ArticleDatabase
import com.example.newsappoffline.core.domain.model.Article
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {



    private var INSTANCE: ArticleDatabase? = null

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): ArticleDatabase {
        if (INSTANCE == null) {
            INSTANCE =
                Room.databaseBuilder(context, ArticleDatabase::class.java, "article.db")
                    .fallbackToDestructiveMigration()
                    .build()
        }
        return INSTANCE!!
    }

    @Provides
    @Singleton
    fun provideArticleDao(database: ArticleDatabase):ArticleDao{
        return database.articleDao
    }


}