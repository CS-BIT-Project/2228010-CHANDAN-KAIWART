package com.example.myapplication.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import androidx.core.net.toUri
import com.example.myapplication.R

class VideoPlayerFragment : Fragment() {

    private val args: VideoPlayerFragmentArgs by navArgs()
    private lateinit var playerView: PlayerView
    private var exoPlayer: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerView = view.findViewById(R.id.playerView)
        initializeExoPlayer()

        val videoUrl = args.videoUrl // Get the video URL from arguments
        if (videoUrl.isNotEmpty()) {
            playVideo(videoUrl)
        } else {
            showError("No video URL available for this recipe")
        }
    }

    private fun initializeExoPlayer() {
        exoPlayer = ExoPlayer.Builder(requireContext()).build().apply {
            playerView.player = this
        }
    }

    private fun playVideo(videoUrl: String) {
        try {
            val mediaItem = MediaItem.fromUri(videoUrl.toUri())
            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
        } catch (e: Exception) {
            showError("Failed to play video: ${e.message}")
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer?.release()
        exoPlayer = null
    }
}