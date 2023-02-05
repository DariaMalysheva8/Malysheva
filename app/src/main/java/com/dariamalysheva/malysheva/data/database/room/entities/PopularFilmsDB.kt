package com.dariamalysheva.malysheva.data.database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dariamalysheva.malysheva.domain.entity.Film

@Entity(tableName = "popularFilms")
data class PopularFilmsDB(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "key")
    val key: Int,

    @ColumnInfo(name = "filmId")
    val filmId: Int,

    @ColumnInfo(name = "nameRu")
    val nameRu: String?,

    @ColumnInfo(name = "year")
    val year: String?,

    @ColumnInfo(name = "genres")
    val genres: String,

    @ColumnInfo(name = "countries")
    val countries: String,

    @ColumnInfo(name = "posterUrl")
    val posterUrl: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean
)

fun PopularFilmsDB.toFilm(): Film {

    return Film(
        filmId = filmId,
        nameRu = nameRu,
        year = year,
        genres = genres,
        countries = countries,
        posterUrl = posterUrl,
        isFavorite = isFavorite
    )
}

fun PopularFilmsDB.toFavoriteFilm(): FavoriteFilmsDB {

    return FavoriteFilmsDB(
        key = 0,
        filmId = filmId,
        nameRu = nameRu,
        year = year,
        genres = genres,
        countries = countries,
        posterUrl = posterUrl,
        description = description,
        isFavorite = true
    )
}
