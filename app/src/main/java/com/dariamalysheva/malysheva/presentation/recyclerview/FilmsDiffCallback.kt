package com.dariamalysheva.malysheva.presentation.recyclerview

import androidx.recyclerview.widget.DiffUtil

class FilmsDiffCallback : DiffUtil.ItemCallback<FilmVO>() {

    override fun areItemsTheSame(oldItem: FilmVO, newItem: FilmVO): Boolean {
        return oldItem.filmId == newItem.filmId
    }

    override fun areContentsTheSame(oldItem: FilmVO, newItem: FilmVO): Boolean {
        return oldItem == newItem
    }
}