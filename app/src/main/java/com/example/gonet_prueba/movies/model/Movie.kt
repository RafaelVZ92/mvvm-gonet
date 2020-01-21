package com.example.gonet_prueba.movies.model


import androidx.room.Entity

data class Movie(
    val id: Int = 0,
    val page: Int,
    val totalResults: Int,
    val totalPages: Int,
    val results: List<MovieResult>
)