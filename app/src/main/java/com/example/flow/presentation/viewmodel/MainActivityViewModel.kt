package com.example.flow.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flow.data.model.BookItems
import com.example.flow.data.model.Recent
import com.example.flow.data.repository.PagingSource
import com.example.flow.data.repository.RecentDatabase
import com.example.flow.data.repository.RetrofitBuilder
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofitInterface = RetrofitBuilder.create()
    private val db = RecentDatabase.getInstance(application.applicationContext)!!
    private val _searchResult = MutableLiveData<PagingData<BookItems>>() //페이징 데이터 옵저빙 (화면 회전 시 데이터 손실 방지)
    val searchResult: LiveData<PagingData<BookItems>>
        get() = _searchResult

    //DB에 검색어 저장
    private fun insertRecentSearch(searchText: String) {
        viewModelScope.launch {
            db.recentDao().insert(Recent(searchText))
        }
    }

    //페이징 데이터 스트림 설정
    private suspend fun getSearchData(query: String) = Pager(PagingConfig(pageSize = 10)) {
        PagingSource(retrofitInterface, query, getApplication())
    }.flow.cachedIn(viewModelScope).collect {
        _searchResult.value = it
    }

    //검색어로 검색
    fun search(searchText: String) {
        //검색어가 있을 때만 검색
        if (searchText.isNotEmpty()) {
            viewModelScope.launch() {
                //최근 검색어 DB에 저장
                insertRecentSearch(searchText)
                //페이징 데이터로 값 넘기기
                getSearchData(searchText)
            }
        }
    }
}