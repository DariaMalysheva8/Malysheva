package com.dariamalysheva.malysheva.presentation.favoritesFilms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dariamalysheva.malysheva.databinding.FragmentFavoritesBinding
import com.dariamalysheva.malysheva.domain.entity.toFilmVO
import com.dariamalysheva.malysheva.presentation.filmDetails.DetailsFragment
import com.dariamalysheva.malysheva.presentation.filmDetails.DetailsFragment.Companion.FAVORITE_FRAGMENT
import com.dariamalysheva.malysheva.presentation.popularFilms.PopularFragment
import com.dariamalysheva.malysheva.presentation.recyclerview.FilmsAdapter
import com.dariamalysheva.malysheva.utils.extensions.navigateToFragment

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding ?: throw RuntimeException("FragmentFavoritesBinding == null")


    private val filmsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FilmsAdapter()
    }

    private val viewModel: FavoritesViewModel by lazy {
        ViewModelProvider(this)[FavoritesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpClickListeners()
        setUpSwipeListener(binding.rvFavoriteFilms)
        setUpObservers()
        updateFilmsList()
    }

    private fun updateFilmsList() {
        viewModel.getFavoriteFilms()
    }

    private fun setUpRecyclerView() {
        with(binding.rvFavoriteFilms) {
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpClickListeners() {
        binding.btnPopularFilms.setOnClickListener {
            navigateToFragment(PopularFragment(), addToBackStack = false)
        }
        filmsAdapter.onFilmClickListener = { filmId ->
            navigateToFragment(
                DetailsFragment.newInstance(filmId, FAVORITE_FRAGMENT),
                addToBackStack = true
            )
        }
        filmsAdapter.onFilmLongClickListener = { filmId, isFavorite ->
            if (!isFavorite) {
                viewModel.deleteFilmFromFavorites(filmId)
            }
        }
    }

    private fun setUpObservers() {
        viewModel.listOfFilms.observe(viewLifecycleOwner) { listOfFilms ->
            filmsAdapter.submitList(listOfFilms.map { film ->
                film.toFilmVO()
            })
        }
    }

    private fun setUpSwipeListener(rvFavoriteFilms: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val film = filmsAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteFilmFromFavorites(film.filmId)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvFavoriteFilms)
    }
}