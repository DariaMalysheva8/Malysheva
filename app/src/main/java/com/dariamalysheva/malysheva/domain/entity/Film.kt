package com.dariamalysheva.malysheva.domain.entity

import com.dariamalysheva.malysheva.data.database.room.entities.PopularFilmsDB
import com.dariamalysheva.malysheva.presentation.recyclerview.FilmVO

data class Film(
    val filmId: Int,
    val nameRu: String?,
    val year: String?,
    val genres: String,
    val countries: String,
    val posterUrl: String,
    var description: String = "",
    var isFavorite: Boolean = false
)

fun Film.toFilmDB(): PopularFilmsDB {

    return PopularFilmsDB(
        key = 0,
        filmId = filmId,
        nameRu = nameRu,
        year = year,
        genres = genres,
        countries = countries,
        posterUrl = posterUrl,
        description = description,
        isFavorite = isFavorite
    )
}

fun Film.toFilmVO(): FilmVO {

    return FilmVO(
        filmId = filmId,
        nameRu = nameRu,
        year = year,
        genres = genres,
        countries = countries,
        posterUrl = posterUrl,
        description = description,
        isFavorite = isFavorite
    )
}
