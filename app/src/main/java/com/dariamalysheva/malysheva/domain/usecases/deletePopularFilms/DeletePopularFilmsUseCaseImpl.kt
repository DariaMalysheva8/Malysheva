package com.dariamalysheva.malysheva.domain.usecases.deletePopularFilms

import com.dariamalysheva.malysheva.domain.repository.FilmsRepository

class DeletePopularFilmsUseCaseImpl(
    private val repository: FilmsRepository
) : DeletePopularFilmsUseCase {

    override suspend fun invoke() {
        return repository.deletePopularFilms()
    }
}