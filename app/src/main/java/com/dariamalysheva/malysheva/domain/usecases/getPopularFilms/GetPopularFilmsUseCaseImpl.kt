package com.dariamalysheva.malysheva.domain.usecases.getPopularFilms

import com.dariamalysheva.malysheva.domain.entity.Film
import com.dariamalysheva.malysheva.domain.repository.FilmsRepository

class GetPopularFilmsUseCaseImpl(
    private val repository: FilmsRepository
) : GetPopularFilmsUseCase {

    override suspend fun invoke(): List<Film> {
        return repository.getPopularFilms()
    }
}