package com.cengizhanerturan.kotlinnewsapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cengizhanerturan.kotlinnewsapplication.databinding.NewsItemBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsHolder>() {
    class NewsHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var onClick: ((newsModel: NewsModel) -> Unit)? = null

    private val diffUtil = object : DiffUtil.ItemCallback<NewsModel>() {
        override fun areItemsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NewsModel, newItem: NewsModel): Boolean {
            return oldItem.title == newItem.title
        }
    }

    private var differ = AsyncListDiffer(this, diffUtil)

    var newsItemList: List<NewsModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    fun setOnClick(func: (newsModel: NewsModel) -> Unit) {
        onClick = func
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsHolder(binding)
    }

    override fun getItemCount(): Int {
        return newsItemList.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val newsModel = newsItemList[position]
        holder.binding.newsModel = newsModel

        holder.itemView.setOnClickListener {
            onClick?.let {
                it(newsModel)
            }
        }
    }
}