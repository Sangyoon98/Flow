package com.example.flow.data.repository

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitBuilder {
    //BASE URL
    private const val BASE_URL = "https://openapi.naver.com/v1/search/"

    //헤더를 달기 위한 Interceptor
    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("X-Naver-Client-Id", "4tOkwcHLckHLUz2rbDMM")
                .addHeader("X-Naver-Client-Secret", "thurvPNNMh")
                .build()
            proceed(newRequest)
        }
    }

    //OKHttp Instance 생성
    private fun okHttpClient(interceptor: AppInterceptor): OkHttpClient =
        OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

    //Retrofit Builder 생성
    fun create(): RetrofitInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)
    }
}