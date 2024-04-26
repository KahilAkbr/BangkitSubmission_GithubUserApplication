package com.example.mygithubapplication.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.mygithubapplication.R
import com.example.mygithubapplication.data.response.ItemsItem

import com.example.mygithubapplication.databinding.ActivityMainBinding
import com.example.mygithubapplication.databinding.ItemListUsersBinding
import com.example.mygithubapplication.ui.adapter.UserAdapter
import com.example.mygithubapplication.ui.favorite.FavoriteActivity
import com.example.mygithubapplication.ui.setting.SettingActivity
import com.example.mygithubapplication.ui.setting.SettingPreferences
import com.example.mygithubapplication.ui.setting.SettingViewModel
import com.example.mygithubapplication.ui.setting.SettingViewModelFactory
import com.example.mygithubapplication.ui.setting.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var itemListUsersBinding : ItemListUsersBinding

    //handling user not found
    private var lastSearchResult: List<ItemsItem> = emptyList()
    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        itemListUsersBinding = ItemListUsersBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        mainActivityBinding.listUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        mainActivityBinding.listUsers.addItemDecoration(itemDecoration)

        mainViewModel.user.observe(this) { user ->
            getUser(user)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(mainActivityBinding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    val searchKeyword = searchBar.text.toString().trim()
                    if (searchKeyword.isNotEmpty()) {
                        searchUser(searchKeyword)
                    } else {
                        searchUser("Kahil")
                    }
                    false
                }
        }

        mainActivityBinding.favorite.setOnClickListener{
            val intent = Intent(this@MainActivity,FavoriteActivity::class.java)
            startActivity(intent)
        }
        mainActivityBinding.setting.setOnClickListener{
            val intent = Intent(this@MainActivity,SettingActivity::class.java)
            startActivity(intent)
        }

        observeThemeSettings()
    }

    private fun observeThemeSettings() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mainActivityBinding.favorite.setImageResource(R.drawable.ic_fav_full_white)
                mainActivityBinding.setting.setImageResource(R.drawable.ic_setting_white)
//                itemListUsersBinding.itemParent.setBackgroundResource(R.color.black)
//                itemListUsersBinding.tvUsername.setBackgroundColor(R.color.white)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mainActivityBinding.favorite.setImageResource(R.drawable.ic_fav_full)
                mainActivityBinding.setting.setImageResource(R.drawable.ic_setting)
//                itemListUsersBinding.itemParent.setBackgroundResource(R.color.white_secondary)
//                itemListUsersBinding.tvUsername.setBackgroundResource(R.color.black)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        mainActivityBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        mainActivityBinding.listUsers.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun searchUser(search: String) {
        mainActivityBinding.listUsers.adapter = adapter

        mainViewModel.searchedUserData(search)

        mainViewModel.user.observe(this) { user ->
            if (user.isEmpty()) {
                if (lastSearchResult.isNotEmpty()) {
                    adapter.submitList(lastSearchResult)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.searchuser_notfound), Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                lastSearchResult = user
                adapter.submitList(user)
            }
        }
    }

    private fun getUser(user: List<ItemsItem>) {
        if (user.isEmpty()) {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.searchuser_notfound), Toast.LENGTH_SHORT
            ).show()

        } else {
            adapter.submitList(user)
            mainActivityBinding.listUsers.adapter = adapter
        }
    }
}