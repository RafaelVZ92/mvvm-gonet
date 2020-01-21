package com.example.gonet_prueba.movies.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class Detail(
    val id: Int,
    val originalTitle: String,
    val status: String,
    @field:Embedded
    val belongsToCollection: BelongsToCollection?,
    @field:Embedded
    val productionCompanies: List<ProductionCountry>,
    @field:Embedded
    val productionCountries: List<ProductionCompany>,
    val overview: String
) {

    data class BelongsToCollection(
        val id: Int,
        val name: String
    )

    data class ProductionCountry(
        var iso31661: String,
        var name: String
    )

    data class ProductionCompany(
        var id: Int,
        var logoPath: String,
        var name: String,
        var originCountry: String
    )
}





