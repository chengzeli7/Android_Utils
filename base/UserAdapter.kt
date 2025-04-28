package com.miaoyin.weiqi.other.base

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.miaoyin.weiqi.databinding.ItemUserBinding

class UserAdapter : BaseAdapter<User, UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // 使用ViewBinding创建ViewHolder
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    inner class UserViewHolder(
        binding: ItemUserBinding
    ) : BindingViewHolder<ItemUserBinding>(binding) {

        override fun <T> bind(item: T) {
            if (item is User) {
                // 设置视图数据
                binding.tvName.text = item.name
                binding.tvDescription.text = item.description

                // 加载头像（使用Glide或其他图片加载库）
                Glide.with(binding.root.context)
                    .load(item.avatar)
                    .circleCrop()
                    .into(binding.ivAvatar)
            }
        }
    }
}

