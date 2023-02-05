package com.dariamalysheva.malysheva.data.network.retrofit.models

data class FilmResponse(
	val films: List<FilmDetailDto>,
	val pagesCount: Int
)
