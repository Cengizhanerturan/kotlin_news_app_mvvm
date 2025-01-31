package com.cengizhanerturan.kotlinnewsapplication.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cengizhanerturan.kotlinnewsapplication.presentation.view.discover.category_news.CategoryListFragment

class CategoryFragmentStateAdapter(
    fragment: Fragment,
    private val categories: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun createFragment(position: Int): Fragment {
        val category = categories[position]
        return CategoryListFragment(category = category)
    }
}