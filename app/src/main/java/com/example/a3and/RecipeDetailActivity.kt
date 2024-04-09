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
import android.content.Intent
import android.net.Uri
import android.widget.Button
import androidx.core.text.HtmlCompat
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
            text = HtmlCompat.fromHtml(recipe.title, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        findViewById<TextView>(R.id.recipe_publisher).apply {
            text = "Posté par : ${recipe.publisher}"
        }

        findViewById<TextView>(R.id.recipe_rating).apply {
            text = "Note : ${recipe.rating}/100"
        }

        val sourceButton: Button = findViewById(R.id.source_button)
        sourceButton.setOnClickListener {
            val sourceUrl = recipe.source_url
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sourceUrl))
            startActivity(intent)
        }
        val ingredientsTextView: TextView = findViewById(R.id.recipe_ingredients)
        ingredientsTextView.text = recipe.ingredients.joinToString("\n")

        findViewById<TextView>(R.id.recipe_date_added).apply {
            text = "Date d'ajout : ${recipe.date_added}"
        }


        val recipeImageView: ImageView = findViewById(R.id.recipe_image)
        Glide.with(this@RecipeDetailActivity)
            .load(recipe.featured_image)
            .into(recipeImageView)
    }
}