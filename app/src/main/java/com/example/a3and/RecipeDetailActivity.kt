package com.example.a3and

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.a3and.model.Recipe
import com.example.a3and.route.getRecipeById
import com.google.gson.Gson
import kotlinx.coroutines.*

class RecipeDetailActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)
        val recipeId = intent.getIntExtra("recipe_id", -1)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val recipe = fetchRecipe(recipeId)
                displayRecipeDetails(recipe)
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private suspend fun fetchRecipe(recipeId: Int): Recipe {
        return withContext(Dispatchers.IO) {
            val recipeJson = getRecipeById(recipeId)
            Gson().fromJson(recipeJson, Recipe::class.java)
        }
    }

    private fun displayRecipeDetails(recipe: Recipe) {
        findViewById<TextView>(R.id.recipe_title).text = recipe.title
        findViewById<TextView>(R.id.recipe_publisher).text = recipe.publisher
        findViewById<TextView>(R.id.recipe_rating).text = recipe.rating.toString()
        findViewById<TextView>(R.id.recipe_source_url).text = recipe.source_url
        findViewById<TextView>(R.id.recipe_description).text = recipe.description ?: "N/A"
        findViewById<TextView>(R.id.recipe_cooking_instructions).text = recipe.cooking_instructions ?: "N/A"
        findViewById<TextView>(R.id.recipe_ingredients).text = recipe.ingredients.joinToString(", ")
        findViewById<TextView>(R.id.recipe_date_added).text = recipe.date_added
        findViewById<TextView>(R.id.recipe_date_updated).text = recipe.date_updated

        val recipeImageView: ImageView = findViewById(R.id.recipe_image)
        Glide.with(this@RecipeDetailActivity)
            .load(recipe.featured_image)
            .into(recipeImageView)
    }
}