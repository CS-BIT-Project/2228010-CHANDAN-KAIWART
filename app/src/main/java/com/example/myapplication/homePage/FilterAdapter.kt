package com.example.recipeapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Filter

class FilterAdapter(
    private val filters: List<Filter>,
    private val onItemClick: (Filter) -> Unit
) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val filterName: TextView = view.findViewById(R.id.tvFilterName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = filters[position]
        holder.filterName.text = filter.name

        holder.itemView.setOnClickListener {
            onItemClick(filter)
        }
    }

    override fun getItemCount() = filters.size
}
