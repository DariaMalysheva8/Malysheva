package com.dariamalysheva.malysheva.presentation.filmDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dariamalysheva.malysheva.databinding.FragmentDetailsBinding
import com.dariamalysheva.malysheva.presentation.favoritesFilms.FavoritesFragment
import com.dariamalysheva.malysheva.presentation.popularFilms.PopularFragment
import com.dariamalysheva.malysheva.utils.extensions.navigateToFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentDetailsBinding == null")

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setOnClickListeners()
    }

    private fun initViews() {
        if (getFilmId() != DEFAULT_FILM_ID) {
            viewModel.getFilm(getFilmId())
        }
        viewModel.film.observe(viewLifecycleOwner) { film ->
            with(binding) {
                tvFilmTitle.text = film.nameRu
                tvFilmDescription.text = film.description
                tvGenresValue.text = film.genres
                tvCountriesValue.text = film.countries.toString()
                CoroutineScope(Dispatchers.Main).launch {
                    Glide.with(binding.root)
                        .load(film.posterUrl)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .fitCenter()
                        .into(ivFilmImage)
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.ivErrorBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            navigateToFragment(getNavigationFragment(), addToBackStack = false)
        }
    }

    private fun getFilmId(): Int {
        return arguments?.getInt(FILM_ID, DEFAULT_FILM_ID) ?: DEFAULT_FILM_ID
    }

    private fun getNavigationFragment(): Fragment {
        val fragmentName =
            arguments?.getString(FROM_FRAGMENT_KEY, POPULAR_FRAGMENT) ?: POPULAR_FRAGMENT

        return if (fragmentName == POPULAR_FRAGMENT) {
            PopularFragment()
        } else {
            FavoritesFragment()
        }
    }

    companion object {

        private const val FILM_ID = "FILM_ID"
        private const val DEFAULT_FILM_ID = 0
        private const val FROM_FRAGMENT_KEY = "FROM_FRAGMENT_KEY"
        const val POPULAR_FRAGMENT = "Popular Fragment"
        const val FAVORITE_FRAGMENT = "Favorites Fragment"

        fun newInstance(filmId: Int, fromFragment: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(FILM_ID, filmId)
                    putString(FROM_FRAGMENT_KEY, fromFragment)
                }
            }
    }
}