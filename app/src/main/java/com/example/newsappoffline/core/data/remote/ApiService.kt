package com.example.newsappoffline.core.data.remote

import com.example.newsappoffline.BuildConfig
import com.example.newsappoffline.core.data.model.NewsListDto
import com.example.newsappoffline.utils.END_POINT
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(END_POINT)
    suspend fun getNewsData(
        @Query("apikey") apiKey : String = BuildConfig.API_KEY,
        @Query("q") query : String ="language",
        @Query("nextPage") nextPage : String? = null
    ):NewsListDto
}