package com.dariamalysheva.malysheva.data.repository

import android.app.Application
import com.dariamalysheva.malysheva.R
import com.dariamalysheva.malysheva.data.database.room.database.AppDatabase
import com.dariamalysheva.malysheva.data.database.room.entities.toFavoriteFilm
import com.dariamalysheva.malysheva.data.database.room.entities.toFilm
import com.dariamalysheva.malysheva.data.network.retrofit.clients.FilmsClient
import com.dariamalysheva.malysheva.data.network.retrofit.models.toFilm
import com.dariamalysheva.malysheva.domain.entity.Film
import com.dariamalysheva.malysheva.domain.entity.toFilmDB
import com.dariamalysheva.malysheva.domain.repository.FilmsRepository
import com.dariamalysheva.malysheva.utils.ResultResponse
import com.dariamalysheva.malysheva.utils.constants.Constants.Companion.TOP_100_POPULAR_FILMS

class FilmsRepositoryImpl(private val application: Application) : FilmsRepository {

    private val filmsDao = AppDatabase.getDatabase(application).getFilmsDao()

    override suspend fun loadFilmsFromNetwork(): ResultResponse {
        filmsDao.deleteAllPopularFilms()
        for (i in 1..6) {
            try {
                val filmResponse = FilmsClient.apiService.getFilms(
                    type = TOP_100_POPULAR_FILMS,
                    page = i
                )
                if (filmResponse.isSuccessful) {
                    val films = filmResponse.body()?.films?.let { listDto ->
                        listDto.map { filmDto ->
                            filmDto.toFilm()
                        }
                    }
                    filmsDao.saveFilmsToPopular(films?.map { film ->
                        film.isFavorite = filmsDao.existsInFavoriteFilms(film.filmId)
                        film.toFilmDB()
                    } ?: listOf())
                } else {
                    return ResultResponse.Failure
                }
            } catch (e: Exception) {
                return ResultResponse.Failure
            }
        }

        return ResultResponse.Success
    }

    override suspend fun getPopularFilms(): List<Film> {

        return filmsDao.getPopularFilms().map { popularFilmsDB ->
            popularFilmsDB.toFilm()
        }
    }

    override suspend fun deletePopularFilms() {
        filmsDao.deleteAllPopularFilms()
    }

    override suspend fun getFavoriteFilms(): List<Film> {

        return filmsDao.getFavoriteFilms().map { favoriteFim ->
            favoriteFim.toFilm()
        }
    }

    override suspend fun saveFilmToFavorites(id: Int) {
        if (filmsDao.existsInPopularFilms(id)) {
            val filmFromPopularDB = filmsDao.getPopularFilmById(id)
            filmFromPopularDB.isFavorite = true
            filmsDao.updatePopularFilms(filmFromPopularDB)
            filmsDao.saveFilmToFavorites(filmFromPopularDB.toFavoriteFilm())
        }
    }

    override suspend fun deleteFilmFromFavorites(id: Int) {
        if (filmsDao.existsInPopularFilms(id)) {
            val filmFromPopularDB = filmsDao.getPopularFilmById(id)
            filmFromPopularDB.isFavorite = false
            filmsDao.updatePopularFilms(filmFromPopularDB)
        }
        if (filmsDao.existsInFavoriteFilms(id)) {
            filmsDao.deleteFilmFromFavoriteById(id)
        }
    }

    override suspend fun loadFilmDetails(id: Int): Film {

        return if (filmsDao.existsInPopularFilms(id)) {
            val filmFromPopularDb = filmsDao.getPopularFilmById(id)
            val film = filmFromPopularDb.toFilm()
            getDescription(id, film)

            film
        } else {
            val filmFromFavoriteDb = filmsDao.getFavoriteFilmById(id)
            val film = filmFromFavoriteDb.toFilm()
            getDescription(id, film)

            film
        }
    }

    private suspend fun getDescription(id: Int, film: Film) {
        try {
            val filmDescriptionDto = FilmsClient.apiService.getFilmDescription(id)
            film.description = filmDescriptionDto.description
        } catch (e: Exception) {
            film.description = application.getString(R.string.error_description)
        }
    }
}