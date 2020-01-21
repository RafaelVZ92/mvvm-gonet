package com.example.gonet_prueba.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gonet_prueba.movies.model.Detail
import com.example.gonet_prueba.movies.model.MovieResult

@Database(
    entities = [MovieResult::class, Detail::class],
    version = 1
)
abstract class MoviesDb : RoomDatabase() {

    abstract fun movieDao() : MovieDao
    abstract fun detailDao(): DetailDao
}