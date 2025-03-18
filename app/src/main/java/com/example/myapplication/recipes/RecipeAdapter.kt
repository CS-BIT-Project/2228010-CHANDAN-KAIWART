package com.example.myapplication.recipes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R

class RecipeAdapter(private var recipes: MutableList<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val playerView: PlayerView = itemView.findViewById(R.id.playerView)
        var exoPlayer: ExoPlayer? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        // Set recipe title and image
        holder.recipeTitle.text = recipe.title
        Glide.with(holder.itemView.context).load(recipe.image).into(holder.recipeImage)

        holder.exoPlayer?.release() // Ensure previous player instance is released
        holder.exoPlayer = null

        if (!recipe.videoUrl.isNullOrEmpty()) {
            holder.playerView.visibility = View.VISIBLE
            holder.exoPlayer = ExoPlayer.Builder(holder.itemView.context).build().apply {
                setMediaItem(MediaItem.fromUri(recipe.videoUrl))
                prepare()
                playWhenReady = false // Do not autoplay
            }
            holder.playerView.player = holder.exoPlayer
        } else {
            holder.playerView.visibility = View.GONE
        }
    }


    override fun getItemCount() = recipes.size

    override fun onViewRecycled(holder: RecipeViewHolder) {
        super.onViewRecycled(holder)
        holder.exoPlayer?.release() // Release player when view is recycled
        holder.exoPlayer = null
    }

    // ✅ Add this method to update adapter data
    fun updateData(newRecipes: List<Recipe>) {
        recipes.clear()  // Clear old data
        recipes.addAll(newRecipes) // Add new data
        notifyDataSetChanged() // Refresh RecyclerView
    }
}