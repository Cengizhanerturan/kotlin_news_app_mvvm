package com.cengizhanerturan.kotlinnewsapplication.presentation.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.cengizhanerturan.kotlinnewsapplication.data.model.toNewsModelList
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.model.Resource
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.NewsRepository
import com.cengizhanerturan.kotlinnewsapplication.domain.service.UserService
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_MSG
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.SLIDER_ITEM_COUNT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userService: UserService,
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _sliderNews: MutableLiveData<Resource<List<NewsModel>>> = MutableLiveData()
    val sliderNews: LiveData<Resource<List<NewsModel>>>
        get() = _sliderNews

    var recommendationPagingFlow: Flow<PagingData<NewsModel>> =
        newsRepository.getTopHeadlinesWithPaging().flow

    init {
        getSliderNews()
    }

    fun getUserNameSurname(): String {
        val user = userService.user
        return user?.let {
            return "${it.name} ${it.surname}"
        } ?: ""
    }

    suspend fun refreshData() {
        coroutineScope {
            val sliderNewsDeferred = async { handleSliderNews() }
            sliderNewsDeferred.await()
        }
    }

    private fun getSliderNews() = viewModelScope.launch {
        try {
            handleSliderNews()
        } catch (e: Exception) {
            _sliderNews.postValue(Resource.Error(message = e.localizedMessage ?: ERROR_MSG))
        }
    }

    private suspend fun handleSliderNews() {
        try {
            _sliderNews.postValue(Resource.Loading())
            val newsResponseList = newsRepository.getTopHeadlines(pageSize = SLIDER_ITEM_COUNT)
            val newsModelList = newsResponseList.toNewsModelList()
            _sliderNews.postValue(Resource.Success(data = newsModelList))
        } catch (e: Exception) {
            throw e
        }
    }
}