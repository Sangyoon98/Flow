package com.example.flow.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.flow.data.model.Recent
import com.example.flow.data.repository.RecentDatabase

class RecentActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var db = RecentDatabase.getInstance(application.applicationContext)!!
    private var recentDao = db.recentDao()

    //최근 10개 데이터 로드
    suspend fun getRecentList(): List<Recent> {
        return recentDao.getRecentList()
    }

    //검색어로 기존에 저장된 데이터 삭제
    suspend fun deleteRecent(search: String) {
        recentDao.deleteRecent(search)
    }
}