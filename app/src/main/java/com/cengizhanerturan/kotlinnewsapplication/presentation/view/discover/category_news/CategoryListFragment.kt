package com.cengizhanerturan.kotlinnewsapplication.presentation.view.discover.category_news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cengizhanerturan.kotlinnewsapplication.core.util.applyListLoading
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentCategoryListBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.model.Resource
import com.cengizhanerturan.kotlinnewsapplication.presentation.adapter.NewsAdapter
import com.cengizhanerturan.kotlinnewsapplication.presentation.view.discover.DiscoverFragmentDirections
import com.cengizhanerturan.kotlinnewsapplication.presentation.view.discover.DiscoverViewModel

class CategoryListFragment(
    private val category: String
) : Fragment() {
    private var _binding: FragmentCategoryListBinding? = null

    private val viewModel: DiscoverViewModel by activityViewModels()

    private lateinit var newsAdapter: NewsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoadingLayout()
        setupAdapter(view)
        observeData()
    }

    private fun setLoadingLayout() {
        binding.loadingContainer.applyListLoading()
    }


    private fun observeData() = viewModel.newsCache.observe(viewLifecycleOwner) { resource ->
        when (resource) {
            is Resource.Success -> {
                val newsModelList = resource.data[category]
                newsAdapter.newsItemList = newsModelList ?: listOf()

                binding.rvNews.visibility = View.VISIBLE
                binding.loadingContainer.visibility = View.GONE
            }

            is Resource.Loading -> {
                newsAdapter.newsItemList = listOf()

                binding.rvNews.visibility = View.GONE
                binding.loadingContainer.visibility = View.VISIBLE
            }

            is Resource.Error -> {
                newsAdapter.newsItemList = listOf()

                binding.rvNews.visibility = View.GONE
                binding.loadingContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun setupAdapter(view: View) {
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        newsAdapter.setOnClick {
            navigateToNewsDetails(view, it)
        }
    }

    private fun navigateToNewsDetails(view: View, newsModel: NewsModel) {
        val action =
            DiscoverFragmentDirections.actionDiscoverFragmentToNewsDetailFragment(newsModel)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}