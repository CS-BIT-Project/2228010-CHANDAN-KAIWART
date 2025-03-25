package com.example.myapplication.data.model

data class YouTubeSearchResponse(
    val items: List<YouTubeVideoItem>
)

data class YouTubeVideoItem(
    val id: VideoId
)

data class VideoId(
    val videoId: String
)
