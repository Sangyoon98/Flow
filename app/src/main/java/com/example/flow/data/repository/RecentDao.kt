package com.example.flow.data.repository

import androidx.room.*
import com.example.flow.data.model.Recent

@Dao
interface RecentDao {

    /**
    <데이터 추가 로직>
     검색어 입력
        기존 데이터 있는지 확인 후 기존 데이터 있으면 삭제 (최근 검색어에 같은 검색어의 데이터가 없도록)
        최근 10개 데이터 로드
            가져온 데이터가 10개 미만인 경우 -> 새로운 데이터만 추가
            가져온 데이터가 10개인 경우 -> 가장 오래된 데이터 삭제 후 새로운 데이터 추가 (검색어가 증가할 때 마다 DB에 쓰지 않는 데이터가 쌓이는 것을 방지)

     최근 검색어 클릭
        클릭한 검색어 DB에서 삭제 (클릭한 검색어가 최근 순으로 정렬 되도록)
        메인 액티비티로 돌아가, 클린한 검색어로 검색 실행
        검색어 입력 로직을 그대로 실행
    */

    @Insert
    suspend fun insert(recent: Recent) {
        //기존에 저장된 데이터 있는지 확인
        val existingData = getRecent(recent.search)

        //기존 데이터 있으면 삭제
        existingData?.let { deleteRecent(recent.search) }

        //최근 10개 데이터 로드
        val recentList = getRecentList()

        //가져온 데이터 10개 미만인 경우, 새로운 데이터 추가
        if (recentList.size < 10) {
            insertRecent(recent)
            return
        }

        //가져온 데이터 10개인 경우, 가장 오래된 데이터 삭제한 후 새로운 데이터 추가
        deleteOldRecentList()
        insertRecent(recent)
    }

    //데이터 추가
    @Insert
    suspend fun insertRecent(recent: Recent)

    //최근 10개 데이터 로드
    @Query("SELECT * FROM Recent ORDER BY createdTime DESC LIMIT 10")
    suspend fun getRecentList(): List<Recent>

    //최근 10개 제외한 데이터 삭제
    @Query("DELETE FROM Recent WHERE id NOT IN (SELECT id FROM Recent ORDER BY createdTime DESC LIMIT 10)")
    suspend fun deleteOldRecentList()

    //검색어로 기존에 저장된 데이터 로드
    @Query("SELECT * FROM Recent WHERE search=:search")
    suspend fun getRecent(search: String): Recent?

    //검색어로 기존에 저장된 데이터 삭제
    @Query("DELETE FROM Recent WHERE search=:search")
    suspend fun deleteRecent(search: String)
}