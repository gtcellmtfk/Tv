package com.example.test_ui_factory.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.utils.BaseListAdapter
import com.example.test_ui_factory.databinding.ItemUserBinding
import com.example.test_ui_factory.entry.User

class UserAdapter : BaseListAdapter<User, UserViewHolder>(UserDiff){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}

class UserViewHolder(val binding:ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
    companion object{
        fun create(parent: ViewGroup): UserViewHolder {
            return UserViewHolder(
                ItemUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}

object UserDiff : DiffUtil.ItemCallback<User>(){
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem==newItem
    }
}