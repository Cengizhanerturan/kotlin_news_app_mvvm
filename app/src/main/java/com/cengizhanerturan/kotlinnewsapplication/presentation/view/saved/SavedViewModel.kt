package com.cengizhanerturan.kotlinnewsapplication.presentation.view.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cengizhanerturan.kotlinnewsapplication.data.model.toNewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.model.Resource
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.NewsRepository
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_MSG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _newsList: MutableLiveData<Resource<List<NewsModel>>> = MutableLiveData()
    val newsList: LiveData<Resource<List<NewsModel>>>
        get() = _newsList

    fun getAllSavedNews() = viewModelScope.launch {
        try {
            _newsList.postValue(Resource.Loading())
            val newsEntityList = newsRepository.getAllSavedNews()
            val newsModelList = newsEntityList.map { newsEntity ->
                newsEntity.toNewsModel()
            }.toList()
            _newsList.postValue(Resource.Success(data = newsModelList))
        } catch (e: Exception) {
            _newsList.postValue(Resource.Error(e.localizedMessage ?: ERROR_MSG))
        }
    }
}