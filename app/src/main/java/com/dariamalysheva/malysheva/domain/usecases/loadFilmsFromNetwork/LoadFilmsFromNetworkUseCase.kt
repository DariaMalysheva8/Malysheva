package com.dariamalysheva.malysheva.domain.usecases.loadFilmsFromNetwork

import com.dariamalysheva.malysheva.utils.ResultResponse

interface LoadFilmsFromNetworkUseCase {

    suspend operator fun invoke(): ResultResponse
}