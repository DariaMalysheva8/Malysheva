package com.dariamalysheva.malysheva.presentation.popularFilms

import android.app.Application
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dariamalysheva.malysheva.data.repository.FilmsRepositoryImpl
import com.dariamalysheva.malysheva.domain.entity.Film
import com.dariamalysheva.malysheva.domain.usecases.deleteFilmFromFavorites.DeleteFilmFromFavoritesUseCase
import com.dariamalysheva.malysheva.domain.usecases.deleteFilmFromFavorites.DeleteFilmFromFavoritesUseCaseImpl
import com.dariamalysheva.malysheva.domain.usecases.deletePopularFilms.DeletePopularFilmsUseCase
import com.dariamalysheva.malysheva.domain.usecases.deletePopularFilms.DeletePopularFilmsUseCaseImpl
import com.dariamalysheva.malysheva.domain.usecases.getPopularFilms.GetPopularFilmsUseCase
import com.dariamalysheva.malysheva.domain.usecases.getPopularFilms.GetPopularFilmsUseCaseImpl
import com.dariamalysheva.malysheva.domain.usecases.loadFilmsFromNetwork.LoadFilmsFromNetworkUseCase
import com.dariamalysheva.malysheva.domain.usecases.loadFilmsFromNetwork.LoadFilmsFromNetworkUseCaseImpl
import com.dariamalysheva.malysheva.domain.usecases.saveFilmToFavorites.SaveFilmToFavoritesUseCase
import com.dariamalysheva.malysheva.domain.usecases.saveFilmToFavorites.SaveFilmToFavoritesUseCaseImpl
import com.dariamalysheva.malysheva.utils.connectivityManager.ConnectivityObserver
import com.dariamalysheva.malysheva.utils.connectivityManager.NetworkConnectivityObserver
import com.dariamalysheva.malysheva.utils.ResultResponse
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PopularViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FilmsRepositoryImpl(application)

    private val getPopularFilmsUseCase: GetPopularFilmsUseCase =
        GetPopularFilmsUseCaseImpl(repository)

    private val loadFilmsFromNetworkUseCase: LoadFilmsFromNetworkUseCase =
        LoadFilmsFromNetworkUseCaseImpl(repository)

    private val deletePopularFilmsUseCase: DeletePopularFilmsUseCase =
        DeletePopularFilmsUseCaseImpl(repository)

    private val saveFilmToFavoritesUseCase: SaveFilmToFavoritesUseCase =
        SaveFilmToFavoritesUseCaseImpl(repository)

    private val deleteFilmFromFavoritesUseCase: DeleteFilmFromFavoritesUseCase =
        DeleteFilmFromFavoritesUseCaseImpl(repository)

    private val connectivityObserver = NetworkConnectivityObserver(application)

    private val _listOfFilms = MutableLiveData<List<Film>>()
    val listOfFilms: LiveData<List<Film>>
        get() = _listOfFilms

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _responseResult = MutableLiveData<ResultResponse>()
    val responseResult: LiveData<ResultResponse>
        get() = _responseResult

    private val _connectivityStatus =
        MutableLiveData<ConnectivityObserver.Status>()
    val connectivityStatus: LiveData<ConnectivityObserver.Status>
        get() = _connectivityStatus


    fun loadFilmsFromNetwork() {
        viewModelScope.launch {
            _loading.value = true
            deletePopularFilmsUseCase()
            if (hasInternetConnection()) {
                val result = loadFilmsFromNetworkUseCase()
                if (result == ResultResponse.Success) {
                    _responseResult.value = ResultResponse.Success
                } else {
                    _responseResult.value = ResultResponse.Failure
                }
            } else {
                _responseResult.value = ResultResponse.Failure
            }

            viewModelScope.launch {
                getPopularFilmsFromDB()
                _loading.value = false
            }
        }
    }

    fun getPopularFilmsFromDB() {
        viewModelScope.launch {
            _listOfFilms.value = getPopularFilmsUseCase()
        }
    }

    fun getConnectivityStatus() {
        connectivityObserver.observe().onEach {
            _connectivityStatus.value = it
        }.launchIn(viewModelScope)
    }

    fun saveFilmToFavorites(id: Int) {
        viewModelScope.launch {
            saveFilmToFavoritesUseCase(id)
        }
    }

    fun deleteFilmFromFavorites(id: Int) {
        viewModelScope.launch {
            deleteFilmFromFavoritesUseCase(id)
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = connectivityObserver.connectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}