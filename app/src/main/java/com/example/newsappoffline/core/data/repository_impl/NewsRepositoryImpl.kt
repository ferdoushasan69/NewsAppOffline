package com.example.newsappoffline.core.data.repository_impl

import android.annotation.SuppressLint
import android.util.Log
import com.example.newsappoffline.core.data.local.ArticleDao
import com.example.newsappoffline.core.data.mapper.toArticle
import com.example.newsappoffline.core.data.mapper.toArticleEntity
import com.example.newsappoffline.core.data.mapper.toNewsList
import com.example.newsappoffline.core.data.remote.ApiService
import com.example.newsappoffline.core.domain.model.Article
import com.example.newsappoffline.core.domain.model.NewsList
import com.example.newsappoffline.core.domain.repository.NewsRepository
import com.example.newsappoffline.utils.NewsResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
   private val apiService: ApiService,
    private val dao: ArticleDao
) : NewsRepository {

    private val TAG = "NewsRepository"
   private suspend fun getLocalNews(nextPage: String?) : NewsList{
        val localNews = dao.getArticleList()
       Log.d(TAG, "getLocalNews: ${localNews.size} nextPage: $nextPage")

       val localNewsList = NewsList(
           nextPage = nextPage?:"",
           results = localNews.map { it.toArticle() }
       )
       return localNewsList

    }
    override suspend fun getAllNews(): Flow<NewsResponse<NewsList>> = flow {
        emit(NewsResponse.Loading())
       val remoteList =  try {
          val data =   apiService.getNewsData(nextPage = null)
           data.toNewsList()
        }catch (e : Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            Log.d(TAG, "getAllNews: ${e.message}")
           null
        }

       remoteList?.let { it ->

           dao.clearDatabase()
        dao.upsertArticleList(it.results.map { it.toArticleEntity() })

        val localNewsList = getLocalNews(remoteList.nextPage)
        emit(NewsResponse.Success(localNewsList))
           return@flow
       }

        val localNewsList = getLocalNews(nextPage = null)
        if (localNewsList.results.isNotEmpty()){
            emit(NewsResponse.Success(localNewsList))
            return@flow
        }
        emit(NewsResponse.Error("null"))
    }

    override suspend fun paginate(nextPage: String?): Flow<NewsResponse<NewsList>> = flow {
        val remoteList =  try {
           val data =  apiService.getNewsData(nextPage = nextPage)
            data.toNewsList()
        }catch (e : Exception){
            e.printStackTrace()
            if (e is CancellationException) throw e
            Log.d(TAG, "paginate: ${e.message}")
            emit(NewsResponse.Error(e.message?:"unknown error"))
            null
        }
        remoteList?.let {it->
            dao.upsertArticleList(it.results.map {it.toArticleEntity()  })


            emit(NewsResponse.Success(it))
            return@flow
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override suspend fun getArticle(articleId: String): Flow<NewsResponse<Article>> = flow{
            dao.getArticleId(articleId = articleId).let {localArticle->
                if (localArticle != null) {
                    Log.d(TAG, "getArticle from local storage: ${localArticle.article_id}")
                    emit(NewsResponse.Success(localArticle.toArticle()))
                    return@flow
                }
            }

            try {

              val response = apiService.getNewsData()
                if (response.toNewsList().results.isNotEmpty()){
                    Log.d(TAG, "getArticle: ${response.results}")
                    emit(NewsResponse.Success(response.results?.get(0)?.toArticle()))
                }else{
                    emit(NewsResponse.Error("error to fetch article id"))
                }
            }catch (e : Exception){
                e.printStackTrace()
                if (e is CancellationException) throw e
                emit(NewsResponse.Error("${e.message}"))
            }


    }
}