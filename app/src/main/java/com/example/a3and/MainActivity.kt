package com.example.a3and

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.a3and.route.getRecipeBySearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            try {
                val recipeJson = withContext(Dispatchers.IO)
                {

                    //Juste pour test
                    for(i in 1..10)
                    {
                    println(getRecipeBySearch("beef"))
                }
                }
                print(recipeJson)
            } catch (e: Exception) {
                print("pas good")
            }
        }
    }
}