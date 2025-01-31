package com.cengizhanerturan.kotlinnewsapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cengizhanerturan.kotlinnewsapplication.databinding.NewsItemBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel

class NewsPagingDataAdapter :
    PagingDataAdapter<NewsModel, NewsPagingDataAdapter.ViewHolder>(NewsPagingDiffCallback()) {
    class ViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var onClick: ((newsModel: NewsModel) -> Unit)? = null

    fun setOnClick(func: (newsModel: NewsModel) -> Unit) {
        onClick = func
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsModel = getItem(position)
        holder.binding.newsModel = newsModel

        holder.itemView.setOnClickListener {
            onClick?.let {
                it(newsModel!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}

private class NewsPagingDiffCallback : DiffUtil.ItemCallback<NewsModel>() {
    override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
        return oldItem.title == newItem.title
    }
}