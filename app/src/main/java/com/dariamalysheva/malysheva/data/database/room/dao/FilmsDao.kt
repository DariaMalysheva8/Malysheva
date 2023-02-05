package com.dariamalysheva.malysheva.data.database.room.dao

import androidx.room.*
import com.dariamalysheva.malysheva.data.database.room.entities.FavoriteFilmsDB
import com.dariamalysheva.malysheva.data.database.room.entities.PopularFilmsDB

@Dao
interface FilmsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveFilmsToPopular(films: List<PopularFilmsDB>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveFilmToPopular(films: PopularFilmsDB)

    @Query("SELECT * FROM popularFilms")
    suspend fun getPopularFilms(): List<PopularFilmsDB>

    @Query("SELECT * FROM popularFilms WHERE `filmId` = :id")
    suspend fun getPopularFilmById(id: Int): PopularFilmsDB

    @Query("DELETE FROM popularFilms")
    suspend fun deleteAllPopularFilms()

    @Update
    suspend fun updatePopularFilms(film: PopularFilmsDB)

    @Query("SELECT * FROM favoriteFilms")
    suspend fun getFavoriteFilms(): List<FavoriteFilmsDB>

    @Query("SELECT * FROM favoriteFilms WHERE `filmId` = :id")
    suspend fun getFavoriteFilmById(id: Int): FavoriteFilmsDB

    @Insert
    suspend fun saveFilmToFavorites(film: FavoriteFilmsDB)

    @Query("DELETE FROM favoriteFilms WHERE `filmId`=:id")
    suspend fun deleteFilmFromFavoriteById(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM popularFilms WHERE `filmId` = :id)")
    suspend fun existsInPopularFilms(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM favoriteFilms WHERE `filmId` = :id)")
    suspend fun existsInFavoriteFilms(id: Int): Boolean
}