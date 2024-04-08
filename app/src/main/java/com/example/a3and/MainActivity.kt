package com.example.a3and

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a3and.adapter.RecipeAdapter
import com.example.a3and.model.Recipe
import com.example.a3and.route.getRecipeById
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity(), RecipeAdapter.OnItemClickListener {

    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        recipeRecyclerView.layoutManager = layoutManager
        recipeRecyclerView.setHasFixedSize(true)

        val recipes = mutableListOf<Recipe>()
        recipeAdapter = RecipeAdapter(recipes,this,this)
        recipeRecyclerView.adapter = recipeAdapter

        recipeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    currentPage++
                    loadRecipes(currentPage)
                }
            }
        })

        loadRecipes(currentPage)
    }

    private fun loadRecipes(page: Int) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    for (i in 1..10) {
                        val recipeId = ((page - 1) * 10) + i
                        val recipeJson = getRecipeById(recipeId)
                        val recipe = Gson().fromJson(recipeJson, Recipe::class.java)
                        withContext(Dispatchers.Main) {
                            recipeAdapter.recipeList.add(recipe)
                            recipeAdapter.notifyItemInserted(recipeAdapter.recipeList.size - 1)
                        }
                    }
                }
            } catch (e: Exception) {
                println("Erreur lors de la récupération des recettes: ${e.message}")
            }
        }
    }

    override fun onItemClick(recipe: Recipe) {
    }
}