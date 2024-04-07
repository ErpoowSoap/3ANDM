package com.example.a3and.route

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun getRecipeBySearch(recipe: String): String {
    val apiUrl = "https://food2fork.ca/api/recipe/search/?query=$recipe"
    val url = URL(apiUrl)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("Authorization", "Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
    connection.doInput = true

    val responseCode = connection.responseCode
    if (responseCode == HttpURLConnection.HTTP_OK) {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readText()
        reader.close()
        return response
    } else {
        throw Exception("Failed to fetch recipe: HTTP error code $responseCode")
    }
}
