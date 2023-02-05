package com.dariamalysheva.malysheva.domain.usecases.loadFilmsFromNetwork

import com.dariamalysheva.malysheva.domain.repository.FilmsRepository
import com.dariamalysheva.malysheva.utils.ResultResponse

class LoadFilmsFromNetworkUseCaseImpl(
    private val repository: FilmsRepository
) : LoadFilmsFromNetworkUseCase {

    override suspend fun invoke(): ResultResponse {
        return repository.loadFilmsFromNetwork()
    }
}