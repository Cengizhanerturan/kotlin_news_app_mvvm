package com.cengizhanerturan.kotlinnewsapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cengizhanerturan.kotlinnewsapplication.databinding.SliderItemBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.model.SliderModel

class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    class SliderViewHolder(val binding: SliderItemBinding) : RecyclerView.ViewHolder(binding.root)

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

    var sliderItemList: List<NewsModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    fun setOnClick(func: (newsModel: NewsModel) -> Unit) {
        onClick = func
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = SliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return sliderItemList.size
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val newsModel = sliderItemList[position]
        holder.binding.newsModel = newsModel

        holder.itemView.setOnClickListener {
            onClick?.let {
                it(newsModel)
            }
        }
    }
}