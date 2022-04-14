package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.ItemsItem
import com.example.githubuser.databinding.ItemRowUserBinding

class ListUserAdapter :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private var list = ArrayList<ItemsItem>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setList(users: ArrayList<ItemsItem>){
        list.clear()
        list.addAll(users)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClick(list[holder.adapterPosition])
        }
    }

    override fun getItemCount() = list.size

    class ListViewHolder(private var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: ItemsItem) {
            binding.tvItemName.text = user.login
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.imgItemPhoto)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClick(user : ItemsItem)
    }
}