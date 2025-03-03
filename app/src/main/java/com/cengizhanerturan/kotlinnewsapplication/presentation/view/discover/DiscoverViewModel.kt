package com.cengizhanerturan.kotlinnewsapplication.presentation.view.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cengizhanerturan.kotlinnewsapplication.data.model.toNewsModelList
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.model.Resource
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.NewsRepository
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_MSG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _newsList: MutableLiveData<Resource<Map<String, List<NewsModel>>>> =
        MutableLiveData()
    val newsList: LiveData<Resource<Map<String, List<NewsModel>>>>
        get() = _newsList

    val tabTitleList = arrayListOf(
        "All",
        "Sports",
        "Health",
        "Science",
        "Technology",
        "Business",
        "Entertainment"
    )

    init {
        getData()
    }

    private fun getData() = viewModelScope.launch {
        try {
            _newsList.postValue(Resource.Loading())
            val tempNewsMap = mutableMapOf<String, List<NewsModel>>()
            tabTitleList.forEachIndexed { index, category ->
                val newsResponse =
                    newsRepository.getTopHeadlines(category = if (index == 0) null else category)
                tempNewsMap[category] = newsResponse.toNewsModelList()
            }
            _newsList.postValue(Resource.Success(data = tempNewsMap))
        } catch (e: Exception) {
            _newsList.postValue(Resource.Error(e.localizedMessage ?: ERROR_MSG))
        }
    }
}