package com.cengizhanerturan.kotlinnewsapplication.presentation.view.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentDiscoverBinding
import com.cengizhanerturan.kotlinnewsapplication.presentation.adapter.CategoryFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DiscoverFragment : Fragment() {
    private var _binding: FragmentDiscoverBinding? = null

    private val viewModel: DiscoverViewModel by activityViewModels()

    private lateinit var adapter: CategoryFragmentStateAdapter

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
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupTabLayout()

        binding.tabBarView.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                viewModel.getData(index = position)
            }
        })

        binding.searchBar.setOnClickListener {
            val action = DiscoverFragmentDirections.actionDiscoverFragmentToSearchFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.tabBarView) { tab, position ->
            tab.text = viewModel.tabTitleList[position]
        }.attach()

        for (i in 0..viewModel.tabTitleList.size) {
            val textView =
                LayoutInflater.from(context).inflate(R.layout.tabbar_item, null) as TextView
            binding.tabLayout.getTabAt(i)?.let {
                it.customView = textView
            }
        }
    }

    private fun setupAdapter() {
        adapter = CategoryFragmentStateAdapter(this, viewModel.tabTitleList)
        binding.tabBarView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}