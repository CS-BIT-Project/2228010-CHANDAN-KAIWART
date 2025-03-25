package com.example.myapplication.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.Recipe
import com.example.myapplication.ui.search.SearchFragmentDirections

class RecipeAdapter(private val onVideoClick: (Recipe) -> Unit) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
    private val recipes = mutableListOf<Recipe>()

    fun submitList(newRecipes: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.recipeImage)
        val title: TextView = view.findViewById(R.id.recipeTitle)
        val videoBtn: Button = view.findViewById(R.id.watchVideoBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.title.text = recipe.title
        Glide.with(holder.image.context).load(recipe.image).into(holder.image)
        holder.videoBtn.setOnClickListener { onVideoClick(recipe) }
    }

    override fun getItemCount(): Int = recipes.size
}