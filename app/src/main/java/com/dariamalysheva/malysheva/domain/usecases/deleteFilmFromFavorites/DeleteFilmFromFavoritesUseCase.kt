package com.dariamalysheva.malysheva.domain.usecases.deleteFilmFromFavorites

interface DeleteFilmFromFavoritesUseCase {

    suspend operator fun invoke(id: Int)
}