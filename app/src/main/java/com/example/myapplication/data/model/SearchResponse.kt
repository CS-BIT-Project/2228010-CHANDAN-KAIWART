package com.example.myapplication.data.model

data class SearchResponse(
    val recipes: List<Recipe>,
    val results: List<Recipe>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)