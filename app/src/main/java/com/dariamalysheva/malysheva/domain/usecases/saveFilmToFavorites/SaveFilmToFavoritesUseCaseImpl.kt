package com.dariamalysheva.malysheva.domain.usecases.saveFilmToFavorites

import com.dariamalysheva.malysheva.domain.repository.FilmsRepository

class SaveFilmToFavoritesUseCaseImpl(
    private val repository: FilmsRepository
) : SaveFilmToFavoritesUseCase {

    override suspend fun invoke(id: Int) {
        repository.saveFilmToFavorites(id)
    }
}