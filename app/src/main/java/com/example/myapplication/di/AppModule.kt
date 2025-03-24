package com.example.myapplication.di

import com.example.myapplication.data.api.SpoonacularApiService
import com.example.myapplication.data.repository.RecipeRepository
import com.example.myapplication.data.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideSpoonacularApiService(okHttpClient: OkHttpClient): SpoonacularApiService {
        return Retrofit.Builder()
            .baseUrl(SpoonacularApiService.BASE_URL) // Use BASE_URL from SpoonacularApiService
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(SpoonacularApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(apiService: SpoonacularApiService): RecipeRepository {
        return RecipeRepositoryImpl(apiService)
    }
}