package com.example.myapplication.homePage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryIcon: ImageView = view.findViewById(R.id.ivCategoryIcon)
        val categoryName: TextView = view.findViewById(R.id.tvCategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        holder.categoryName.text = category.name
        holder.categoryIcon.setImageResource(category.iconResId)

        holder.itemView.setOnClickListener {
            onItemClick(category)
        }
    }

    override fun getItemCount() = categories.size
}
