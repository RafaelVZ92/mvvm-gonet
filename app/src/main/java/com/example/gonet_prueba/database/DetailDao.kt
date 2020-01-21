package com.example.gonet_prueba.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gonet_prueba.movies.model.Detail


@Dao
interface DetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(detail: Detail)

    @Query("SELECT * FROM Detail WHERE id = :id")
    fun getMovieDetail(id: Int): LiveData<Detail>
}