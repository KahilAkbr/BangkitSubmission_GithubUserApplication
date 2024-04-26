package com.example.mygithubapplication.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubapplication.data.database.entity.FavoriteUser
import com.example.mygithubapplication.data.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository : FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavUser()
}