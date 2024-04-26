package com.example.mygithubapplication.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mygithubapplication.data.database.entity.FavoriteUser
import com.example.mygithubapplication.data.database.room.FavoriteUserDao
import com.example.mygithubapplication.data.database.room.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao : FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavUser()
}