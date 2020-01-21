package com.example.gonet_prueba.repository

import androidx.lifecycle.LiveData
import com.example.gonet_prueba.AppExecutors
import com.example.gonet_prueba.database.MovieDao
import com.example.gonet_prueba.movies.http.ApiResponse
import com.example.gonet_prueba.movies.http.MoviesServicesApi
import com.example.gonet_prueba.movies.model.Movie
import com.example.gonet_prueba.movies.model.MovieResult
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject internal constructor(
    var movieDao: MovieDao,
    var appExecutors: AppExecutors,
    var moviesServicesApi: MoviesServicesApi
    ) {

    val API_KEY = "08425cd314576d2ad6072d596f49b16c"
    val LANGUAGE = "en-US"
    val PAGE = 1

    var popular:String = "Popular"
    val topRate:String = "top_rated"

    fun loadMovie(category: String): LiveData<ResourceStatus<MovieResult>>{
        return object : NetworkBoundResource<MovieResult, Movie>(appExecutors){
            override fun saveCallResult(item: Movie) {
                for (MovieResult in item.results){
                    movieDao.insert(movie = MovieResult)
                }
            }

            override fun shouldFetch(data: MovieResult?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<MovieResult> {
                return movieDao.getMoviesByCategory(category)
            }

            override fun createCall(): LiveData<ApiResponse<Movie>> {
                return moviesServicesApi.getMoviesPopular(API_KEY, LANGUAGE, PAGE)
            }

        }.coverterLiveData()
    }

}