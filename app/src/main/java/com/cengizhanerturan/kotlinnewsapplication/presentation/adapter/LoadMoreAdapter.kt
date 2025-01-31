package com.cengizhanerturan.kotlinnewsapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cengizhanerturan.kotlinnewsapplication.databinding.LoadMoreLayoutBinding

class LoadMoreAdapter : LoadStateAdapter<LoadMoreAdapter.ViewHolder>() {
    class ViewHolder(val binding: LoadMoreLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.binding.loading.isVisible = loadState is LoadState.Loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding =
            LoadMoreLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}