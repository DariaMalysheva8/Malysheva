package com.dariamalysheva.malysheva.domain.usecases.getFavoriteFilms

import com.dariamalysheva.malysheva.domain.entity.Film
import com.dariamalysheva.malysheva.domain.repository.FilmsRepository

class GetFavoriteFilmsUseCaseImpl(
    private val repository: FilmsRepository
) : GetFavoriteFilmsUseCase {

    override suspend fun invoke(): List<Film> {
        return repository.getFavoriteFilms()
    }
}