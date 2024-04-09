package com.example.a3and

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.a3and.route.getRecipeById
import com.example.a3and.model.Recipe


class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        loadRecipes()
    }

    object RecipeHolder {
        val recipes = mutableListOf<Recipe>()
    }

    private fun loadRecipes() {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    // Load the first 10 recipes.
                    for (i in 1..10) {
                        val recipeId = i
                        val recipeJson = getRecipeById(recipeId)
                        val recipe = mutableListOf<Recipe>()
                        //val recipe = Gson().fromJson(recipeJson, Recipe::class.java)
                        RecipeHolder.recipes.add(recipe)
                        // Add the recipe to your data source.
                        // ...
                    }

                    // Navigate to MainActivity once the recipes are loaded.
                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                        finish()
                    }
                }
            } catch (e: Exception) {
                println("Error loading recipes: ${e.message}")
            }
        }
    }
}

private fun <E> MutableList<E>.add(element: MutableList<E>) {

}

