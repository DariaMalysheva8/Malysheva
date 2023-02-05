package com.dariamalysheva.malysheva.domain.usecases.getPopularFilms

import com.dariamalysheva.malysheva.domain.entity.Film

interface GetPopularFilmsUseCase {

    suspend operator fun invoke(): List<Film>
}