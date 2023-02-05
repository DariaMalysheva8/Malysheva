package com.dariamalysheva.malysheva.data.network.retrofit.models

import com.dariamalysheva.malysheva.domain.entity.Film

data class FilmDetailDto(
    val filmId: Int,
    val nameRu: String?,
    val year: String? = "",
    val genres: List<GenresItemDto>,
    val countries: List<CountriesItemDto>,
    val posterUrl: String
)

fun FilmDetailDto.toFilm(): Film {
    val genres = if (genres.size > 1) {
        "${genres[0].genre.replaceFirstChar { it.uppercase() }}, ${genres[1].genre}"
    } else {
        genres[0].genre.replaceFirstChar { it.uppercase() }
    }
    val countries = if (countries.size > 1) {
        "${countries[0].country}, ${countries[1].country}"
    } else {
        countries[0].country
    }

    return Film(
        filmId = filmId,
        nameRu = nameRu,
        year = year,
        genres = genres,
        countries = countries,
        posterUrl = posterUrl
    )
}