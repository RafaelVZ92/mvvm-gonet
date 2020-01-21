package com.example.gonet_prueba.movies.model

import androidx.room.Entity

@Entity(primaryKeys = ["id", "title","category"])
data class MovieResult(
    val popularity: Double,
    val posterPath: String,
    val id: Int,
    val originalLanguage: String,
    val title: String,
    val releaseDate: String,
    var category:String?)