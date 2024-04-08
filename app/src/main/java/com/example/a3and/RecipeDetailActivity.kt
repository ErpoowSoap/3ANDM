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
import android.text.util.Linkify
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
        findViewById<TextView>(R.id.recipe_title).apply {
            text = "${recipe.title}"
        }

        findViewById<TextView>(R.id.recipe_publisher).apply {
            text = "Auteur : ${recipe.publisher}"
        }

        findViewById<TextView>(R.id.recipe_rating).apply {
            text = "Évaluation : ${recipe.rating}/100"
        }

        val sourceUrlTextView: TextView = findViewById(R.id.recipe_source_url)
        sourceUrlTextView.apply {
            text = "Source : ${recipe.source_url}"
            Linkify.addLinks(this, Linkify.WEB_URLS)
        }

        findViewById<TextView>(R.id.recipe_description).apply {
            text = "Description : ${recipe.description ?: "N/A"}"
        }

        findViewById<TextView>(R.id.recipe_cooking_instructions).apply {
            text = "Instructions de cuisson : ${recipe.cooking_instructions ?: "N/A"}"
        }

        val ingredientsTextView: TextView = findViewById(R.id.recipe_ingredients)
        ingredientsTextView.text = recipe.ingredients.joinToString("\n")
        findViewById<TextView>(R.id.recipe_date_added).apply {
            text = "Date d'ajout : ${recipe.date_added}"
        }

        findViewById<TextView>(R.id.recipe_date_updated).apply {
            text = "Date de mise à jour : ${recipe.date_updated}"
        }

        val recipeImageView: ImageView = findViewById(R.id.recipe_image)
        Glide.with(this@RecipeDetailActivity)
            .load(recipe.featured_image)
            .into(recipeImageView)
    }
}