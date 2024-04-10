package com.example.a3and.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val publisher: String,
    val source_url: String,
    val rating: Int,
    val featured_image: String,
    val ingredients: List<String>,
    val date_added: String,
)