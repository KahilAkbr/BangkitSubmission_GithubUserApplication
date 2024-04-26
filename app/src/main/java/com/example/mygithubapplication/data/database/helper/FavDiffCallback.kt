package com.example.mygithubapplication.data.database.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.mygithubapplication.data.database.entity.FavoriteUser

class FavDiffCallback(private val oldFavList: List<FavoriteUser>, private val newFavList: List<FavoriteUser>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldFavList.size
    }

    override fun getNewListSize(): Int {
        return newFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavList[oldItemPosition].username == newFavList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFav = oldFavList[oldItemPosition]
        val newFav = newFavList[newItemPosition]
        return oldFav.username == newFav.username && oldFav.avatarUrl == newFav.avatarUrl
    }
}