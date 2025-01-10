package com.cengizhanerturan.kotlinnewsapplication.presentation.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.core.util.applyListLoading
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentHomeBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.model.Resource
import com.cengizhanerturan.kotlinnewsapplication.presentation.MainActivity
import com.cengizhanerturan.kotlinnewsapplication.presentation.adapter.NewsAdapter
import com.cengizhanerturan.kotlinnewsapplication.presentation.adapter.SliderAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    //Bindings
    private var _binding: FragmentHomeBinding? = null

    //ViewModel
    private val viewModel by activityViewModels<HomeViewModel>()

    //Slider
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var indicatorList: List<ImageView>
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback

    //Recommendation
    private lateinit var recommendationAdapter: NewsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLoadingLayout()
        setNameSurname()
        setPullToRefresh()
        setupSlider(view)
        setupRecommendationAdapter(view)
        searchButtonClicked(view)
        observeSliderNews()
        observeRecommendationNews()
        viewAllClicked()
    }

    private fun setPullToRefresh() {
        binding.pullToRefresh.apply {
            setColorSchemeResources(R.color.primary)
            setProgressBackgroundColorSchemeResource(R.color.card_background)
            onRefresh()
        }
    }

    private fun SwipeRefreshLayout.onRefresh() {
        setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.refreshData()
                isRefreshing = false
            }
        }
    }

    private fun setLoadingLayout() {
        binding.recommendationLayout.loadingContainer.applyListLoading()
    }

    private fun searchButtonClicked(view: View) {
        binding.homeTopBar.searchButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController(view).navigate(action)
        }
    }

    private fun viewAllClicked() {
        binding.breakingNewsTitle.viewAllButton.setOnClickListener {
            navigateToDiscover()
        }
        binding.recommendationLayout.recommendationNewsTitle.viewAllButton.setOnClickListener {
            navigateToDiscover()
        }
    }

    private fun navigateToDiscover() {
        val activity = requireActivity() as MainActivity
        activity.binding.bottomNavigationView.selectedItemId = R.id.discoverFragment
    }

    private fun observeRecommendationNews() =
        viewModel.recommendationList.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    recommendationAdapter.newsItemList = resource.data

                    binding.recommendationLayout.rvRecommendation.visibility = View.VISIBLE
                    binding.recommendationLayout.loadingContainer.visibility = View.GONE
                }

                is Resource.Loading -> {
                    binding.recommendationLayout.loadingContainer.visibility = View.VISIBLE
                    binding.recommendationLayout.rvRecommendation.visibility = View.GONE
                }

                is Resource.Error -> {
                    binding.recommendationLayout.loadingContainer.visibility = View.VISIBLE
                    binding.recommendationLayout.rvRecommendation.visibility = View.GONE
                }
            }
        }

    private fun observeSliderNews() = viewModel.sliderNews.observe(viewLifecycleOwner) { resource ->
        when (resource) {
            is Resource.Success -> {
                sliderAdapter.sliderItemList = resource.data

                setupIndicator(sliderAdapter.sliderItemList.size)
                binding.sliderLayout.slider.visibility = View.VISIBLE
                binding.sliderLayout.sliderContainer.visibility = View.GONE
            }

            is Resource.Loading -> {
                binding.sliderLayout.slider.visibility = View.GONE
                binding.sliderLayout.sliderContainer.visibility = View.VISIBLE
            }

            is Resource.Error -> {
                binding.sliderLayout.slider.visibility = View.GONE
                binding.sliderLayout.sliderContainer.visibility = View.VISIBLE
            }
        }
    }

    private fun setNameSurname() {
        binding.homeTopBar.userNameSurname.text = viewModel.getUserNameSurname()
    }

    private fun setupRecommendationAdapter(view: View) {
        recommendationAdapter = NewsAdapter()
        binding.recommendationLayout.rvRecommendation.apply {
            adapter = recommendationAdapter
            layoutManager = LinearLayoutManager(context)
        }
        recommendationAdapter.setOnClick { newsModel ->
            navigateToNewsDetail(view = view, newsModel = newsModel)
        }
    }

    private fun navigateToNewsDetail(view: View, newsModel: NewsModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToNewsDetailFragment(newsModel)
        findNavController(view).navigate(action)
    }


    private fun setupSlider(view: View) {
        sliderAdapter = SliderAdapter()
        binding.sliderLayout.slider.apply {
            adapter = sliderAdapter
        }
        setupPageChangeListener()
        sliderAdapter.setOnClick {
            navigateToNewsDetail(view, it)
        }
    }

    private fun setupPageChangeListener() {
        pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicatorList.forEachIndexed { index, imageView ->
                    if (index == position) {
                        imageView.setImageResource(R.drawable.indicator_active)
                    } else {
                        imageView.setImageResource(R.drawable.indicator)
                    }
                }
            }
        }

        binding.sliderLayout.slider.registerOnPageChangeCallback(pageChangeListener)
    }

    private fun setupIndicator(size: Int) {
        binding.sliderLayout.sliderIndicator.removeAllViews()

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }

        indicatorList = List(size) {
            ImageView(context)
        }

        indicatorList.forEach {
            it.setImageResource(
                R.drawable.indicator
            )
            binding.sliderLayout.sliderIndicator.addView(it, params)
        }
        indicatorList[0].setImageResource(R.drawable.indicator_active)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.sliderLayout.slider.unregisterOnPageChangeCallback(pageChangeListener)
        _binding = null
    }
}