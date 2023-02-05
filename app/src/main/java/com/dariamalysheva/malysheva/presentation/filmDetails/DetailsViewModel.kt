package com.dariamalysheva.malysheva.presentation.filmDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dariamalysheva.malysheva.data.repository.FilmsRepositoryImpl
import com.dariamalysheva.malysheva.domain.entity.Film
import com.dariamalysheva.malysheva.domain.usecases.loadFilmDetails.LoadFilmDetailsUseCase
import com.dariamalysheva.malysheva.domain.usecases.loadFilmDetails.LoadFilmDetailsUseCaseImpl
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FilmsRepositoryImpl(application)

    private val loadFilmDetailsUseCase: LoadFilmDetailsUseCase =
        LoadFilmDetailsUseCaseImpl(repository)

    private val _film = MutableLiveData<Film>()
    val film: LiveData<Film>
        get() = _film

    fun getFilm(id: Int) {
        viewModelScope.launch {
            _film.value = loadFilmDetailsUseCase(id)
        }
    }
}