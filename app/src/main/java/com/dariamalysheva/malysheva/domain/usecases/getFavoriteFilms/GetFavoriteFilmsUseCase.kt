package com.dariamalysheva.malysheva.domain.usecases.getFavoriteFilms

import com.dariamalysheva.malysheva.domain.entity.Film

interface GetFavoriteFilmsUseCase {

    suspend operator fun invoke(): List<Film>
}