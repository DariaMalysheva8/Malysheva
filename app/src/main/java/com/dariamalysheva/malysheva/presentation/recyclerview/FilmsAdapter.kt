package com.dariamalysheva.malysheva.presentation.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dariamalysheva.malysheva.databinding.FilmsRecyclerViewItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmsAdapter : ListAdapter<FilmVO, FilmsAdapter.FilmViewHolder>(FilmsDiffCallback()) {

    var onFilmLongClickListener: ((Int, Boolean) -> Unit)? = null
    var onFilmClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FilmsRecyclerViewItemBinding.inflate(inflater, parent, false)

        return FilmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = getItem(position)
        holder.bind(film)
        holder.itemView.setOnClickListener {
            onFilmClickListener?.invoke(film.filmId)
        }
        holder.itemView.setOnLongClickListener {
            onFilmLongClickListener?.invoke(film.filmId, !film.isFavorite)
            holder.binding.ivIsFavorite.visibility = getIsFavoriteVisibility(film)
            true
        }
    }

    private fun getIsFavoriteVisibility(film: FilmVO): Int {
        film.isFavorite = !film.isFavorite

        return if (film.isFavorite) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    class FilmViewHolder(
        val binding: FilmsRecyclerViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(film: FilmVO) {
            with(binding) {
                tvFilmTitle.text = film.nameRu
                val genresAndYear = "${film.genres} (${film.year})"
                tvGenreAndIssueYear.text = genresAndYear
                CoroutineScope(Dispatchers.Main).launch {
                    Glide.with(binding.root)
                        .load(film.posterUrl)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .fitCenter()
                        .into(ivFilmImage)
                }
                setFavoriteVisibility(film.isFavorite, ivIsFavorite)
            }
        }

        private fun setFavoriteVisibility(isFavorite: Boolean, iv: ImageView) {
            if (isFavorite) {
                iv.visibility = View.VISIBLE
            } else {
                iv.visibility = View.GONE
            }
        }
    }
}