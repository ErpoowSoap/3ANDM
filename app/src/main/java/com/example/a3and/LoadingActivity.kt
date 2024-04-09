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
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.gson.Gson

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        if (isConnectedToInternet()) {
            loadRecipes()
        } else {
            startActivity(Intent(this, NoInternetActivity::class.java))
            finish()
        }
    }

    object RecipeHolder {
        val recipes = mutableListOf<Recipe>()
    }

    private fun loadRecipes() {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    for (i in 1..10) {
                        val recipeId = i
                        val recipeJson = getRecipeById(recipeId)
                        val recipe = Gson().fromJson(recipeJson, Recipe::class.java)
                        RecipeHolder.recipes.add(recipe)
                    }
                }
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                println("Error loading recipes: ${e.message}")
            }
        }
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
