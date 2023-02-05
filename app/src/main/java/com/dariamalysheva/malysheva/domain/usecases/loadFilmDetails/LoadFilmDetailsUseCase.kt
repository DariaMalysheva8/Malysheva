package com.dariamalysheva.malysheva.domain.usecases.loadFilmDetails

import com.dariamalysheva.malysheva.domain.entity.Film

interface LoadFilmDetailsUseCase {

    suspend operator fun invoke(id: Int): Film
}