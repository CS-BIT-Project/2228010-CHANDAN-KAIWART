package com.example.myapplication.data.model

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val summary: String,
    val instructions: String
)
data class RecipeDetailed(
    val title: String,
    val image: String,
    val summary: String,
    val instructions: String
)
