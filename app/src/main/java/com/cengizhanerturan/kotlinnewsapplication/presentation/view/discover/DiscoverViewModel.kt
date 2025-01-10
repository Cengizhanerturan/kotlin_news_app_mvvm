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
    private val _newsCache = mutableMapOf<String, List<NewsModel>>()
    private val _newsList: MutableLiveData<Resource<List<NewsModel>>> = MutableLiveData()
    val newsList: LiveData<Resource<List<NewsModel>>>
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

    fun getData(index: Int) = viewModelScope.launch {
        try {
            val category = tabTitleList[index].lowercase()
            if (_newsCache.containsKey(category)) {
                _newsCache[category]?.let { cachedNews ->
                    _newsList.postValue(Resource.Success(data = cachedNews))
                }
            } else {
                _newsList.postValue(Resource.Loading())
                val newsResponse =
                    newsRepository.getTopHeadlines(category = if (index == 0) null else category)
                _newsCache[category] = newsResponse.toNewsModelList()
                _newsList.postValue(Resource.Success(data = newsResponse.toNewsModelList()))
            }
        } catch (e: Exception) {
            _newsList.postValue(Resource.Error(e.localizedMessage ?: ERROR_MSG))
        }

    }
}