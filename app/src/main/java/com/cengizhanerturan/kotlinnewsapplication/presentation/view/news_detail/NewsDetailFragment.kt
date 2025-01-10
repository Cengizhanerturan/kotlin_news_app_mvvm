package com.cengizhanerturan.kotlinnewsapplication.presentation.view.news_detail

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentNewsDetailBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.NEWS_MODEL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {
    private var _binding: FragmentNewsDetailBinding? = null

    private lateinit var newsModel: NewsModel
    private val viewModel: NewsDetailViewModel by viewModels()

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
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNewsModel()
        checkNews()
        observeSaveNews()
        saveButtonClickListener()
        backButtonListener()
        readMoreButtonClickListener(view)
    }

    private fun readMoreButtonClickListener(view: View) {
        binding.readMoreButton.setOnClickListener {
            //openUrlInBrowser(newsModel.url)
            val action =
                NewsDetailFragmentDirections.actionNewsDetailFragmentToWebViewFragment(newsModel.url)
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun checkNews() {
        viewModel.checkNews(newsModel.title)
    }

    private fun saveButtonClickListener() = binding.savedButton.setOnClickListener {
        val isSaved = viewModel.isNewsSaved.value
        if (isSaved == true) {
            viewModel.deleteNews(newsModel.title)
        } else {
            viewModel.saveNews(newsModel)
        }
    }


    private fun observeSaveNews() = viewModel.isNewsSaved.observe(viewLifecycleOwner) { isSaved ->
        if (isSaved) {
            binding.savedButton.setImageResource(R.drawable.saved_active)
        } else {
            binding.savedButton.setImageResource(R.drawable.saved)
        }
    }

    private fun setNewsModel() {
        arguments?.let { bundle ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(NEWS_MODEL, NewsModel::class.java)?.let {
                    binding.newsModel = it
                    newsModel = it
                }
            } else {
                bundle.getSerializable(NEWS_MODEL)?.let {
                    binding.newsModel = it as NewsModel
                    newsModel = it
                }
            }
        }
    }

    private fun backButtonListener() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}