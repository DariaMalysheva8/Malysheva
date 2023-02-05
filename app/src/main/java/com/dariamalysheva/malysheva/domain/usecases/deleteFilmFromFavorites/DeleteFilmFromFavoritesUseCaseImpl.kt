package com.dariamalysheva.malysheva.domain.usecases.deleteFilmFromFavorites

import com.dariamalysheva.malysheva.domain.repository.FilmsRepository

class DeleteFilmFromFavoritesUseCaseImpl(
    private val repository: FilmsRepository
) : DeleteFilmFromFavoritesUseCase {

    override suspend fun invoke(id: Int) {
        repository.deleteFilmFromFavorites(id)
    }
}