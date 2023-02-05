package com.dariamalysheva.malysheva.data.network.retrofit.services

import com.dariamalysheva.malysheva.data.network.retrofit.models.FilmDescriptionDto
import com.dariamalysheva.malysheva.data.network.retrofit.models.FilmResponse
import com.dariamalysheva.malysheva.utils.constants.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmsApiService {

    @Headers("$X_API_KEY: $API_KEY")
    @GET("/api/v2.2/films/top")
    suspend fun getFilms(
        @Query(TYPE) type: String,
        @Query(PAGE) page: Int
    ): Response<FilmResponse>

    @Headers("$X_API_KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}")
    suspend fun getFilmDescription(
        @Path(ID) id: Int
    ): FilmDescriptionDto

    companion object {
        private const val X_API_KEY = "X-API-KEY"
        private const val TYPE = "type"
        private const val PAGE = "page"
        private const val ID = "id"
    }
}