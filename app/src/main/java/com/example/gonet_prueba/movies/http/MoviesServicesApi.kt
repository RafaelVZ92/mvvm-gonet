package com.example.gonet_prueba.movies.http

import androidx.lifecycle.LiveData
import com.example.gonet_prueba.movies.model.Detail
import com.example.gonet_prueba.movies.model.Movie
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesServicesApi {

    @GET("movie/popular")
    fun getMoviesPopular(
        @Query("api_key") key: String,
        @Query("language") language: String, @Query("page") page: Int
    ): LiveData<ApiResponse<Movie>>

    @GET("movie/top_rated")
    fun getMoviesTopRated(
        @Query("api_key") key: String,
        @Query("language") language: String, @Query("page") page: Int
    ): LiveData<ApiResponse<Movie>>

    @GET("movie/upcoming")
    fun getMoviesUpcoming(
        @Query("api_key") key: String,
        @Query("language") language: String, @Query("page") page: Int
    ): LiveData<ApiResponse<Movie>>

    @GET("movie/{movie-id}")
    fun getMoviesDetail(@Path("movie-id") id: Int): Observable<ApiResponse<LiveData<Detail>>>
}