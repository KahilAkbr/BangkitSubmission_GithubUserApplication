package com.example.mygithubapplication.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubapplication.R
import com.example.mygithubapplication.data.database.entity.FavoriteUser
import com.example.mygithubapplication.data.database.room.FavoriteUserRoomDatabase
import com.example.mygithubapplication.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("NAME_SHADOWING")
class DetailUserActivity : AppCompatActivity() {
    private lateinit var detailUserBinding: ActivityDetailUserBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    private var isFavorite : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailUserBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailUserBinding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra(USER_DATA)
        detailUserViewModel.getDetail(username!!)

        detailUserViewModel.avatar.observe(this) { avatar ->
            setAvatar(avatar)
        }
        detailUserViewModel.username.observe(this) { username ->
            setUsername(username)
        }
        detailUserViewModel.name.observe(this) { name ->
            setName(name)
        }
        detailUserViewModel.follower.observe(this) { follower ->
            setFollower(follower)
        }
        detailUserViewModel.following.observe(this) { following ->
            setFollowing(following)
        }

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager_detail)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tab_follower_following)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        getFavUser(username)

        detailUserBinding.addFav.setOnClickListener{
            isFavorite = !isFavorite
            if(isFavorite){
                detailUserBinding.addFav.setImageResource(R.drawable.ic_fav_full)
                val user = FavoriteUser(username = username, avatarUrl = detailUserViewModel.avatar.value)
                insertToDatabase(user)
            }else{
                detailUserBinding.addFav.setImageResource(R.drawable.ic_fav_null)
                deleteFromDatabase(username)
            }
        }
    }

    private fun deleteFromDatabase(username: String) {
        val userDao = FavoriteUserRoomDatabase.getDatabase(this).favoriteUserDao()
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUsername(username)
            user.let {
                userDao.delete(it)
            }
        }
    }

    private fun getFavUser(username: String?) {
        val userDao = FavoriteUserRoomDatabase.getDatabase(this).favoriteUserDao()
        CoroutineScope(Dispatchers.IO).launch {
            val favIcon = username?.let { userDao.getUsername(it) }
            withContext(Dispatchers.Main){
                isFavorite = favIcon != null
                detailUserBinding.addFav.setImageResource(if (isFavorite) R.drawable.ic_fav_full else R.drawable.ic_fav_null)
            }
        }
    }

    private fun insertToDatabase(user: FavoriteUser) {
        val userDao = FavoriteUserRoomDatabase.getDatabase(this).favoriteUserDao()
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insert(user)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        detailUserBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setFollowing(following: Int?) {
        detailUserBinding.tvFollowingCount.text = following.toString()
        detailUserBinding.tvFollowing.setText(R.string.following)
    }

    private fun setFollower(follower: Int?) {
        detailUserBinding.tvFollowerCount.text = follower.toString()
        detailUserBinding.tvFollower.setText(R.string.followers)
    }

    private fun setName(name: String?) {
        if (name != null) {
            detailUserBinding.tvName.text = name
        } else {
            detailUserBinding.tvName.setText(R.string.name_not_found)
        }

    }

    private fun setUsername(username: String?) {
        detailUserBinding.tvUsername.text = username
    }

    private fun setAvatar(avatar: String) {
        Glide.with(this@DetailUserActivity)
            .load(avatar)
            .into(detailUserBinding.ivAvatar)
    }

    companion object {
        const val USER_DATA = "user_data"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}