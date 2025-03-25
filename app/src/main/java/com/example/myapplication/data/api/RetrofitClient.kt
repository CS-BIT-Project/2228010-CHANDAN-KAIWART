package com.example.myapplication.data.api

import com.example.myapplication.data.api.SpoonacularApiService
import com.example.myapplication.data.api.YouTubeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val SPOONACULAR_BASE_URL = "https://api.spoonacular.com/"
    private const val YOUTUBE_BASE_URL = "https://www.googleapis.com/"

    private val spoonacularRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SPOONACULAR_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val youtubeRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(YOUTUBE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val spoonacularApi: SpoonacularApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SPOONACULAR_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpoonacularApiService::class.java)
    }

    val youtubeApi: YouTubeApiService = youtubeRetrofit.create(YouTubeApiService::class.java)
}
