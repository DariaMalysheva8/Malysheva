package com.dariamalysheva.malysheva.domain.usecases.loadFilmDetails

import com.dariamalysheva.malysheva.domain.entity.Film
import com.dariamalysheva.malysheva.domain.repository.FilmsRepository

class LoadFilmDetailsUseCaseImpl(
    private val repository: FilmsRepository
) : LoadFilmDetailsUseCase {

    override suspend fun invoke(id: Int): Film {
        return repository.loadFilmDetails(id)
    }
}