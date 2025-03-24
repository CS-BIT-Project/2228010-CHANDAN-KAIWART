package com.example.myapplication.data.model

data class VideoResponse(
    val videos: List<VideoItem>
)

data class VideoItem(
    val title: String,
    val youTubeId: String,
    val thumbnail: String
) {
    fun getVideoUrl(): String {
        return "https://www.youtube.com/watch?v=$youTubeId"
    }
}
