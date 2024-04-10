package com.example.a3and

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SearchView
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
import com.example.a3and.LoadingActivity.*
import com.example.a3and.route.getRecipeBySearch
import org.json.JSONObject
import com.example.a3and.model.RecipeEntity
import com.example.a3and.database.AppDatabase
import androidx.room.Room

class MainActivity : ComponentActivity(), RecipeAdapter.OnItemClickListener {

    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private var currentPage = 1
    private lateinit var originalRecipes: MutableList<Recipe>
    private var recipes: MutableList<Recipe> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = findViewById<SearchView>(R.id.searchView)
        originalRecipes = mutableListOf()
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        recipeRecyclerView.layoutManager = layoutManager
        recipeRecyclerView.setHasFixedSize(true)

        val recipes = mutableListOf<Recipe>()
        recipeAdapter = RecipeAdapter(recipes, this, this)
        recipeRecyclerView.adapter = recipeAdapter

        val buttonAll = findViewById<Button>(R.id.buttonAll)
        val buttonChicken = findViewById<Button>(R.id.buttonChicken)
        val buttonFish = findViewById<Button>(R.id.buttonFish)
        val buttonBeef = findViewById<Button>(R.id.buttonBeef)
        val buttonVanilla = findViewById<Button>(R.id.buttonVanilla)
        val buttonChocolate = findViewById<Button>(R.id.buttonChocolate)

        buttonAll.setOnClickListener {
            loadData("All")
        }

        buttonChicken.setOnClickListener {
            loadData("Chicken")
        }

        buttonFish.setOnClickListener {
            loadData("Fish")
        }

        buttonBeef.setOnClickListener {
            loadData("Beef")
        }

        buttonVanilla.setOnClickListener {
            loadData("Vanilla")
        }

        buttonChocolate.setOnClickListener {
            loadData("Chocolate")
        }

        recipeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    currentPage++
                    loadRecipes(currentPage)
                }
            }
        })

        if (RecipeHolder.recipes.isNotEmpty()) {
            recipeAdapter.recipeList.addAll(RecipeHolder.recipes)
            recipeAdapter.notifyDataSetChanged()
        } else {
            loadRecipes(currentPage)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterRecipes(it)
                }
                return true
            }

            private fun filterRecipes(query: String) {
                loadData(query)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        loadData()
    }
    private fun loadRecipes(page: Int) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    for (i in 1..10) {
                        val recipeId = ((page - 1) * 10) + i
                        val recipeJson = getRecipeById(recipeId)
                        Log.d("MainActivity", "Recipe JSON: $recipeJson")
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

    private fun loadData(search: String = "All") {
        lifecycleScope.launch {
            val list = loadAllRecipes(search)
            recipes.addAll(list)
            clearAdapter()
            addToAdapter(list)
        }
    }
    private suspend fun loadAllRecipes(search: String = "All"): List<Recipe> {
        return withContext(Dispatchers.IO) {
            val recipeJson = getRecipeBySearch(search)
            val jsonObject = JSONObject(recipeJson)
            val jsonArray = jsonObject.getJSONArray("results")
            val list = mutableListOf<Recipe>()
            for (i in 0 until jsonArray.length()) {
                val recipe = Gson().fromJson(
                    jsonArray.getJSONObject(i).toString(),
                    Recipe::class.java
                )
                list.add(recipe)
            }
            val recipeEntities = list.map { RecipeEntity(it.pk, it.title, it.publisher,it.source_url,it.rating,it.featured_image,it.ingredients,it.date_added) }
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name"
            ).fallbackToDestructiveMigration().build()
            db.recipeDao().insertAll(recipeEntities)
            list
        }
    }

    private fun clearAdapter() {
        lifecycleScope.launch {
            recipeAdapter.recipeList.clear()
            recipeAdapter.notifyDataSetChanged()
        }
    }

    private fun addToAdapter(recipes: List<Recipe>) {
        lifecycleScope.launch {
            recipeAdapter.recipeList.addAll(recipes)
            recipeAdapter.notifyItemInserted(recipeAdapter.recipeList.size - 1)
        }
    }


    override fun onItemClick(recipe: Recipe) {
    }
}