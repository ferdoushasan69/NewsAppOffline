package com.example.newsappoffline.utils

sealed class NewsResponse<T>(val data : T?=null,val error : String?=null){
    class Loading<T>:NewsResponse<T>()
    class Success<T>(data: T?):NewsResponse<T>(data = data, error = null)
    class Error<T>(error: String?) : NewsResponse<T>(error = error)
}