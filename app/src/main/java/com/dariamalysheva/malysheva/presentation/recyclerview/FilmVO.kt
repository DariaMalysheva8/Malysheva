package com.dariamalysheva.malysheva.presentation.recyclerview

data class FilmVO(
    val filmId: Int,
    val nameRu: String?,
    val year: String?,
    val genres: String,
    val countries: String,
    val posterUrl: String,
    val description: String,
    var isFavorite: Boolean
)
