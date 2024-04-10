package com.example.a3and.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.a3and.model.RecipeEntity
import com.example.a3and.model.RecipeDao
import com.example.a3and.model.Converters

@Database(entities = [RecipeEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}