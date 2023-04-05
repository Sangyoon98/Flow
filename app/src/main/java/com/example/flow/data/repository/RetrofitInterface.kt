package com.example.flow.data.repository

import com.example.flow.data.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    //네이버 책 API (query = 검색어, start = 검색 시작 위치)
    @GET("book.json")
    suspend fun getBook(
        @Query("query") search: String,
        @Query("start") start: Int
    ): BookResponse
}