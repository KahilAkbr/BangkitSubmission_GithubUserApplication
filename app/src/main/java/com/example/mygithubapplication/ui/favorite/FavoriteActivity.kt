package com.example.mygithubapplication.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubapplication.data.database.entity.FavoriteUser
import com.example.mygithubapplication.databinding.ActivityFavoriteBinding
import com.example.mygithubapplication.ui.adapter.FavoriteAdapter
import com.example.mynoteapps.helper.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteBinding: ActivityFavoriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        val favoriteViewModel = obtainViewModel(this@FavoriteActivity)

        favoriteViewModel.getAllFavUser().observe(this){
            getFav(it)
        }

    }

    private fun getFav(user: List<FavoriteUser>?) {
        val adapter = FavoriteAdapter()
        adapter.getFavUser(user)
        favoriteBinding.listUsers.adapter = adapter
        favoriteBinding.listUsers.layoutManager = LinearLayoutManager(this)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }
}