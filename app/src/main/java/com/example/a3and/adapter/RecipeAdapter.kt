package com.example.a3and.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a3and.R
import com.example.a3and.RecipeDetailActivity
import com.example.a3and.model.Recipe



class RecipeAdapter(val recipeList: MutableList<Recipe>, val listener: OnItemClickListener, val context: Context) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipe: Recipe)
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle: TextView = itemView.findViewById(R.id.recipe_title)
        val recipeImage: ImageView = itemView.findViewById(R.id.recipe_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentItem = recipeList[position]
        holder.recipeTitle.text = currentItem.title

        Glide.with(holder.itemView.context)
            .load(currentItem.featured_image)
            .into(holder.recipeImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("recipe_id", currentItem.pk)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = recipeList.size
}
