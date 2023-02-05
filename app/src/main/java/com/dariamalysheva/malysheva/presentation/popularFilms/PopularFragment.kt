package com.dariamalysheva.malysheva.presentation.popularFilms

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dariamalysheva.malysheva.R
import com.dariamalysheva.malysheva.databinding.FragmentPopularBinding
import com.dariamalysheva.malysheva.domain.entity.toFilmVO
import com.dariamalysheva.malysheva.presentation.favoritesFilms.FavoritesFragment
import com.dariamalysheva.malysheva.presentation.filmDetails.DetailsFragment
import com.dariamalysheva.malysheva.presentation.filmDetails.DetailsFragment.Companion.POPULAR_FRAGMENT
import com.dariamalysheva.malysheva.presentation.recyclerview.FilmsAdapter
import com.dariamalysheva.malysheva.utils.connectivityManager.ConnectivityObserver
import com.dariamalysheva.malysheva.utils.ResultResponse
import com.dariamalysheva.malysheva.utils.constants.Constants.Companion.APP_PREFERENCES
import com.dariamalysheva.malysheva.utils.constants.Constants.Companion.PREF_DEFAULT_NETWORK_VALUE
import com.dariamalysheva.malysheva.utils.constants.Constants.Companion.PREF_FROM_NETWORK_VALUE
import com.dariamalysheva.malysheva.utils.extensions.navigateToFragment

class PopularFragment : Fragment() {

    private var _binding: FragmentPopularBinding? = null
    private val binding: FragmentPopularBinding
        get() = _binding ?: throw RuntimeException("FragmentPopularBinding == null")

    private val filmsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FilmsAdapter()
    }

    private val viewModel: PopularViewModel by lazy {
        ViewModelProvider(this)[PopularViewModel::class.java]
    }

    private val preferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    private lateinit var connectivityStatus: ConnectivityObserver.Status

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fromNetwork =
            preferences.getBoolean(PREF_FROM_NETWORK_VALUE, PREF_DEFAULT_NETWORK_VALUE)
        setUpRecyclerView()
        setUpClickListeners()
        setUpObservers()
        updateFilmsList(fromNetwork)
    }

    private fun updateFilmsList(fromNetwork: Boolean) {
        if (fromNetwork) {
            viewModel.loadFilmsFromNetwork()
        } else {
            viewModel.getPopularFilmsFromDB()
        }
    }

    private fun setUpRecyclerView() {
        with(binding.rvPopularFilms) {
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpClickListeners() {
        filmsAdapter.onFilmClickListener = { filmId ->
            navigateToFragment(
                DetailsFragment.newInstance(filmId, POPULAR_FRAGMENT),
                addToBackStack = true
            )
        }
        filmsAdapter.onFilmLongClickListener = { filmId, isFavorite ->
            if (isFavorite) {
                viewModel.saveFilmToFavorites(filmId)
            } else {
                viewModel.deleteFilmFromFavorites(filmId)
            }
        }
        binding.btnFavoritesFilms.setOnClickListener {
            navigateToFragment(FavoritesFragment(), addToBackStack = false)
        }
        binding.btnRetryLoad.setOnClickListener {
            updateFilmsList(true)
        }
    }

    private fun setUpObservers() {
        viewModel.listOfFilms.observe(viewLifecycleOwner) { listOfFilms ->
            filmsAdapter.submitList(listOfFilms.map { film ->
                film.toFilmVO()
            })
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
        viewModel.responseResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                ResultResponse.Success -> onSuccess()
                ResultResponse.Failure -> onFailure()
                else -> {
                    throw RuntimeException("Unknown Result Response")
                }
            }
        }
        viewModel.connectivityStatus.observe(viewLifecycleOwner) { status ->
            connectivityStatus = status
            when (connectivityStatus) {
                ConnectivityObserver.Status.Unavailable -> showInternetErrorToast()
                ConnectivityObserver.Status.Lost -> showInternetErrorToast()
                ConnectivityObserver.Status.Losing -> showInternetErrorToast()
                ConnectivityObserver.Status.Available -> Unit
            }
        }
        viewModel.getConnectivityStatus()
    }

    private fun showInternetErrorToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.toast_internet_error_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onFailure() {
        with(binding) {
            ivCloud.visibility = View.VISIBLE
            tvErrorMessage.visibility = View.VISIBLE
            btnRetryLoad.visibility = View.VISIBLE
        }
    }

    private fun onSuccess() {
        with(binding) {
            ivCloud.visibility = View.GONE
            tvErrorMessage.visibility = View.GONE
            btnRetryLoad.visibility = View.GONE
        }
        preferences.edit().putBoolean(PREF_FROM_NETWORK_VALUE, false).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}