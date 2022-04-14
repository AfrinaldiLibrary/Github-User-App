package com.example.githubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.Favorite
import com.example.githubuser.databinding.ItemRowUserBinding

class FavoriteUserAdapter:
    RecyclerView.Adapter<FavoriteUserAdapter.ListViewHolder>() {
    private var list = ArrayList<Favorite>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: ArrayList<Favorite>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
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
        fun bind(user: Favorite) {
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
        fun onItemClick(user : Favorite)
    }
}