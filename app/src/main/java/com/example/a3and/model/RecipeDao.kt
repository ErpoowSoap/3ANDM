package com.example.a3and.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Query("SELECT * FROM RecipeEntity")
    suspend fun getAll(): List<RecipeEntity>

    @Query("SELECT COUNT(*) FROM RecipeEntity")
    suspend fun countRecipes(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<RecipeEntity>)
}