package com.cengizhanerturan.kotlinnewsapplication.presentation.view.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cengizhanerturan.kotlinnewsapplication.core.util.applyListLoading
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentSearchBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.model.Resource
import com.cengizhanerturan.kotlinnewsapplication.presentation.adapter.NewsAdapter
import com.cengizhanerturan.kotlinnewsapplication.core.util.clearFocusAndHideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null

    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var newsAdapter: NewsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clearState()
        getDefaultList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clearFocusAndHideKeyboard()
        setLoadingLayout()
        backButtonListener()
        setupRecyclerView(view)
        observeData()
        searchListener()
    }

    private fun setLoadingLayout() {
        binding.loadingContainer.applyListLoading()
    }

    private fun getDefaultList() {
        viewModel.getDefaultList()
    }

    private fun searchListener() {
        binding.searchInput.addTextChangedListener { editable ->
            val searchString = editable.toString()
            if (viewModel.searchString != searchString) {
                viewModel.searchString = searchString
                viewModel.getSearchNews()
            }
        }
    }

    private fun observeData() = viewModel.newsList.observe(viewLifecycleOwner) { resource ->
        when (resource) {
            is Resource.Success -> {
                newsAdapter.newsItemList = resource.data

                binding.rvSearch.visibility = View.VISIBLE
                binding.loadingContainer.visibility = View.GONE
            }

            is Resource.Loading -> {
                binding.loadingContainer.visibility = View.VISIBLE
                binding.rvSearch.visibility = View.GONE
            }

            is Resource.Error -> {
                binding.loadingContainer.visibility = View.VISIBLE
                binding.rvSearch.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        newsAdapter = NewsAdapter()
        binding.rvSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        newsAdapter.setOnClick {
            navigateToNewsDetail(view, it)
        }
    }

    private fun navigateToNewsDetail(view: View, newsModel: NewsModel) {
        val action = SearchFragmentDirections.actionSearchFragmentToNewsDetailFragment(newsModel)
        Navigation.findNavController(view).navigate(action)
    }


    private fun backButtonListener() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
            getDefaultList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}