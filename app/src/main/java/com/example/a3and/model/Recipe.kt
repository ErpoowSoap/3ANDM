package com.example.a3and.model

data class Recipe
    (
    val pk: Int,
    val title: String,
    val publisher: String,
    val featured_image: String,
    val rating: Int,
    val source_url: String,
    val ingredients: List<String>,
    val date_added: String,
)