package com.dariamalysheva.malysheva.domain.usecases.saveFilmToFavorites

interface SaveFilmToFavoritesUseCase {

    suspend operator fun invoke(id: Int)
}