package com.example.myapplication.recipes

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("readyInMinutes") val readyInMinutes: Int,
    @SerializedName("spoonacularScore") val spoonacularScore: Float?,
    @SerializedName("image") val image: String?,
    @SerializedName("videoUrl") val videoUrl: String?

)
