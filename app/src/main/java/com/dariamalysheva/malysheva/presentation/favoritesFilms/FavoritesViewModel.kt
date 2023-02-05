package com.dariamalysheva.malysheva.presentation.favoritesFilms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dariamalysheva.malysheva.data.repository.FilmsRepositoryImpl
import com.dariamalysheva.malysheva.domain.entity.Film
import com.dariamalysheva.malysheva.domain.usecases.deleteFilmFromFavorites.DeleteFilmFromFavoritesUseCase
import com.dariamalysheva.malysheva.domain.usecases.deleteFilmFromFavorites.DeleteFilmFromFavoritesUseCaseImpl
import com.dariamalysheva.malysheva.domain.usecases.getFavoriteFilms.GetFavoriteFilmsUseCase
import com.dariamalysheva.malysheva.domain.usecases.getFavoriteFilms.GetFavoriteFilmsUseCaseImpl
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FilmsRepositoryImpl(application)

    private val getFavoriteFilmsUseCase: GetFavoriteFilmsUseCase =
        GetFavoriteFilmsUseCaseImpl(repository)

    private val deleteFilmFromFavoritesUseCase: DeleteFilmFromFavoritesUseCase =
        DeleteFilmFromFavoritesUseCaseImpl(repository)

    private val _listOfFilms = MutableLiveData<List<Film>>()
    val listOfFilms: LiveData<List<Film>>
        get() = _listOfFilms

    fun getFavoriteFilms() {
        viewModelScope.launch {
            _listOfFilms.value = getFavoriteFilmsUseCase()
        }
    }

    fun deleteFilmFromFavorites(id: Int) {
        viewModelScope.launch {
            deleteFilmFromFavoritesUseCase(id)
            getFavoriteFilms()
        }
    }
}