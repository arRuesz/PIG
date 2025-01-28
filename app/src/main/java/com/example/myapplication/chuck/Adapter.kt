package com.example.myapplication.chuck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class Adapter(
    private val categories: List<String>,
    private val onCategoryClicked: (String) -> Unit
) : RecyclerView.Adapter<Adapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCategory: TextView = itemView.findViewById(R.id.textViewCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.textViewCategory.text = category
        holder.itemView.setOnClickListener { onCategoryClicked(category) }
    }

    override fun getItemCount(): Int = categories.size
}
