package com.example.gonet_prueba.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gonet_prueba.movies.model.Movie
import com.example.gonet_prueba.movies.model.MovieResult

@Dao
interface MovieDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieResult)

    @Query("SELECT * FROM MovieResult WHERE category = :category")
    fun getMoviesByCategory(category:String): LiveData<MovieResult>

    @Query("SELECT * FROM MovieResult WHERE title = :title")
    fun getMoviesByTitle(title:String): LiveData<MovieResult>
}