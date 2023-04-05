package com.example.flow.data.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.paging.PagingState
import androidx.paging.PagingSource
import com.example.flow.data.model.BookItems
import retrofit2.HttpException
import java.io.IOException

class PagingSource(
    private val retrofitInterface: RetrofitInterface,
    private val query: String,
    private val context: Context
) : PagingSource<Int, BookItems>() {

    //첫번째 Item 위치 반환
    override fun getRefreshKey(state: PagingState<Int, BookItems>): Int? {
        return state.anchorPosition
    }

    //페이징 로드
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookItems> {
        val page = params.key ?: 1
        return try {
            //Retrofit 통신
            val response = retrofitInterface.getBook(query, page)   //데이터 요청
            val items = response.items
            val prevKey = if (page == 1) null else page - 10        //이전 페이지
            val nextKey = if (items.isEmpty()) null else page + 10  //다음 페이지
            LoadResult.Page(
                data = items,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {      //네트워크 에러 처리
            Log.e(TAG, "IOException: $e")
            LoadResult.Error(e)
        } catch (e: HttpException) {    //서버 에러 처리
            Log.e(
                TAG, "HttpException: ${e.response()?.code()}\n" +
                        "HttpExceptionBody: ${e.response()?.errorBody()?.string()}"
            )
            LoadResult.Error(e)
        } catch (e: Exception) {        //나머지 예외 처리
            Log.e(TAG, "Exception: $e")
            LoadResult.Error(e)
        }.also {//에러 메시지 출력
            if (it is LoadResult.Error) {
                val message = when (it.throwable) {
                    is IOException -> "네트워크 오류가 발생했습니다."
                    is HttpException -> "서버 오류가 발생했습니다."
                    else -> "알 수 없는 오류가 발생했습니다."
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}