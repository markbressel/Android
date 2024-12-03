package com.tasty.recipesapp.api

import com.tasty.recipesapp.dto.RecipeDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeService {
    @GET("api/recipes")
    suspend fun getRecipes(): List<RecipeDTO> // Adjust based on API response structure

    @GET("api/recipes/{id}")
    suspend fun getRecipeDetails(@Path("id") id: Int): RecipeDTO
}
