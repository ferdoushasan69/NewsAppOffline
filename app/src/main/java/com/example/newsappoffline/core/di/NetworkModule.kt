package com.example.newsappoffline.core.di

import com.example.newsappoffline.core.data.remote.ApiService
import com.example.newsappoffline.core.data.repository_impl.NewsRepositoryImpl
import com.example.newsappoffline.core.domain.repository.NewsRepository
import com.example.newsappoffline.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30,TimeUnit.SECONDS)
        .readTimeout(30,TimeUnit.SECONDS)
        .writeTimeout(30,TimeUnit.SECONDS)
        .build()
    @Singleton
    @Provides
    fun provideApiService():ApiService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(newsRepositoryImpl: NewsRepositoryImpl) : NewsRepository{
        return newsRepositoryImpl
    }
}