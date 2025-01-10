package com.cengizhanerturan.kotlinnewsapplication.presentation.view.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentSavedBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.model.Resource
import com.cengizhanerturan.kotlinnewsapplication.presentation.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private lateinit var newsAdapter: NewsAdapter
    private val viewModel: SavedViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter(view)
        observeData()
        getData()
    }

    private fun getData() {
        viewModel.getAllSavedNews()
    }

    private fun observeData() = viewModel.newsList.observe(viewLifecycleOwner) { resource ->
        when (resource) {
            is Resource.Success -> {
                newsAdapter.newsItemList = resource.data

                binding.savedNewsRV.visibility = View.VISIBLE
                binding.loading.visibility = View.GONE
            }

            is Resource.Loading -> {
                binding.loading.visibility = View.VISIBLE
                binding.savedNewsRV.visibility = View.GONE
            }

            is Resource.Error -> {
                binding.loading.visibility = View.VISIBLE
                binding.savedNewsRV.visibility = View.GONE
            }
        }
    }

    private fun setupAdapter(view: View) {
        newsAdapter = NewsAdapter()
        binding.savedNewsRV.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        newsAdapter.setOnClick { newsModel ->
            navigateToNewsDetail(view, newsModel)
        }
    }

    private fun navigateToNewsDetail(view: View, newsModel: NewsModel) {
        val action = SavedFragmentDirections.actionSavedFragmentToNewsDetailFragment(newsModel)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}