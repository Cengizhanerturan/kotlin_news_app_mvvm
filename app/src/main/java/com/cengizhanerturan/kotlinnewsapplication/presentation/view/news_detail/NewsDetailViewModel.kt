package com.cengizhanerturan.kotlinnewsapplication.presentation.view.news_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cengizhanerturan.kotlinnewsapplication.data.model.toNewsEntity
import com.cengizhanerturan.kotlinnewsapplication.domain.model.NewsModel
import com.cengizhanerturan.kotlinnewsapplication.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _isNewsSaved: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNewsSaved: LiveData<Boolean>
        get() = _isNewsSaved

    fun checkNews(title: String) = viewModelScope.launch {
        try {
            val isSaved = newsRepository.isNewsSaved(title)
            _isNewsSaved.postValue(isSaved)
        } catch (e: Exception) {
            _isNewsSaved.postValue(false)
        }
    }

    fun saveNews(newsModel: NewsModel) = viewModelScope.launch {
        try {
            val newsEntity = newsModel.toNewsEntity()
            val isSaved = newsRepository.saveNews(newsEntity)
            _isNewsSaved.postValue(isSaved)
        } catch (e: Exception) {
            _isNewsSaved.postValue(false)
        }
    }

    fun deleteNews(title: String) = viewModelScope.launch {
        try {
            val isDeleted = newsRepository.deleteNews(title)
            _isNewsSaved.postValue(!isDeleted)
        } catch (e: Exception) {
            _isNewsSaved.postValue(true)
        }
    }
}