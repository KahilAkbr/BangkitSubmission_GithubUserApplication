package com.example.mygithubapplication.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubapplication.data.response.ItemsItem
import com.example.mygithubapplication.databinding.ItemListUsersBinding
import com.example.mygithubapplication.ui.detail.DetailUserActivity

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
        const val USER_DATA = "user_data"
    }

    class MyViewHolder(private val binding: ItemListUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.tvUsername.text = user.login
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.imgItemPhoto)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ItemListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val moveDetail = Intent(holder.itemView.context, DetailUserActivity::class.java)
            moveDetail.putExtra(USER_DATA, user.login)
            holder.itemView.context.startActivity(moveDetail)
        }
    }
}