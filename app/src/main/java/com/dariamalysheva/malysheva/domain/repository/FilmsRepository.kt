package com.dariamalysheva.malysheva.domain.repository

import com.dariamalysheva.malysheva.domain.entity.Film
import com.dariamalysheva.malysheva.utils.ResultResponse

interface FilmsRepository {

    suspend fun loadFilmsFromNetwork(): ResultResponse

    suspend fun getPopularFilms(): List<Film>

    suspend fun deletePopularFilms()

    suspend fun getFavoriteFilms(): List<Film>

    suspend fun saveFilmToFavorites(id: Int)

    suspend fun deleteFilmFromFavorites(id: Int)

    suspend fun loadFilmDetails(id: Int): Film
}