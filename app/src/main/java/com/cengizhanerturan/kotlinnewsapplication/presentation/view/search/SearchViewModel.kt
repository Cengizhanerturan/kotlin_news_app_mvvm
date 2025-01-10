package com.cengizhanerturan.kotlinnewsapplication.presentation.view.search

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    var searchString: String = ""

    private var defaultSearchList: List<NewsModel> = listOf()

    private var _newsList: MutableLiveData<Resource<List<NewsModel>>> = MutableLiveData()
    val newsList: LiveData<Resource<List<NewsModel>>>
        get() = _newsList

    private var job: Job? = null

    fun clearState() {
        _newsList = MutableLiveData()
        searchString = ""
    }

    fun getDefaultList() = viewModelScope.launch {
        try {
            if (defaultSearchList.isEmpty()) {
                _newsList.postValue(Resource.Loading())
                val newsResponse = newsRepository.getTopHeadlines()
                defaultSearchList = newsResponse.toNewsModelList()
            }
            _newsList.postValue(Resource.Success(data = defaultSearchList))
        } catch (e: Exception) {
            _newsList.postValue(Resource.Error(message = e.localizedMessage ?: ERROR_MSG))
        }

    }

    fun getSearchNews() {
        try {
            job?.cancel()
            if (searchString.isNotEmpty()) {
                job = viewModelScope.launch(Dispatchers.IO) {
                    _newsList.postValue(Resource.Loading())
                    delay(500)
                    viewModelScope.launch {
                        val newsResponse = newsRepository.getSearchNews(searchString)
                        _newsList.postValue(Resource.Success(data = newsResponse.toNewsModelList()))
                    }
                }
            } else {
                _newsList.postValue(Resource.Success(data = defaultSearchList))
            }
        } catch (_: CancellationException) {
        } catch (e: Exception) {
            _newsList.postValue(Resource.Error(message = e.localizedMessage ?: ERROR_MSG))
        }
    }

}