package com.example.newsappoffline.news.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.newsappoffline.core.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.newsappoffline.utils.NewsResponse
import kotlinx.coroutines.launch

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    var state by mutableStateOf(NewsState())
        private set

    init {
        fetchNews()
    }

    fun onAction(newsAction: NewsAction){
        when(newsAction){
            is NewsAction.Paginate->{
                Log.d(TAG, "onAction: paginate action triggered")
                paginate()
            }
        }
    }

    private fun paginate() {
        viewModelScope.launch {
        Log.d(TAG, "paginate: Started")
            state = state.copy(isLoading = true)
            newsRepository.paginate(state.nextPage).collect{response->
              state =  when(response){
                    is NewsResponse.Success->{
                        Log.d(TAG, "paginate Success: ${response.data?.results?.size} item ")
                        val articleList = response.data?.results?: emptyList()
                         state.copy(
                            article = state.article + articleList,
                            nextPage = response.data?.nextPage
                        )
                    }
                    is NewsResponse.Error->{
                        Log.d(TAG, "paginate Error: ${response.error}")
                         state.copy(error = true, isLoading = false)

                    }

                  is NewsResponse.Loading -> {
                      state.copy(isLoading = true)
                  }
              }

            }
            state = state.copy(isLoading = false)
            Log.d(TAG, "paginate: End")
        }
    }

    private fun fetchNews(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            newsRepository.getAllNews().collect{response->
               state = when(response){
                    is NewsResponse.Success->{
                        Log.d(TAG, "fetchNews: ${response.data?.nextPage}")
                         state.copy(article = response.data?.results?: emptyList(),
                             nextPage = response.data?.nextPage
                         )
                    }
                    is NewsResponse.Error->{
                        state.copy(error = true, isLoading = false)
                    }

                   is NewsResponse.Loading -> {
                       state.copy(isLoading = true)
                   }
               }
            }
            state = state.copy(isLoading = false)
        }
    }

}