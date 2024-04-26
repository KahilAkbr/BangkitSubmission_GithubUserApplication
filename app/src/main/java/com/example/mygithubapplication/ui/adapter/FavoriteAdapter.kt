package com.example.mygithubapplication.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubapplication.data.database.entity.FavoriteUser
import com.example.mygithubapplication.data.database.helper.FavDiffCallback
import com.example.mygithubapplication.databinding.ItemListUsersBinding
import com.example.mygithubapplication.ui.detail.DetailUserActivity

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {

    private var userList = listOf<FavoriteUser>()

    inner class ListViewHolder(private val binding: ItemListUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: FavoriteUser) {
            with(binding) {
                tvUsername.text = user.username
                Glide.with(root.context)
                    .load(user.avatarUrl)
                    .into(imgItemPhoto)

                root.setOnClickListener {
                    val moveDetail = Intent(it.context, DetailUserActivity::class.java)
                    moveDetail.putExtra(USER_DATA, user.username)
                    it.context.startActivity(moveDetail)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun getFavUser(newList: List<FavoriteUser>?) {
        val diffCallback = FavDiffCallback(userList, newList ?: emptyList())
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.userList = newList ?: emptyList()
        diffResult.dispatchUpdatesTo(this)
    }

    companion object {
        const val USER_DATA = "user_data"
    }
}