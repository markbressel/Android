package com.tasty.recipesapp.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.tasty.recipesapp.database.RecipeDao
import com.tasty.recipesapp.database.RecipeEntity
import com.tasty.recipesapp.dto.*
import com.tasty.recipesapp.models.*
import com.tasty.recipesapp.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class RecipeRepository(
    private val context: Context,
    private val recipeDao: RecipeDao
) {
    private val gson = Gson()
    private val apiClient = RetrofitClient.recipeService

    // Fetch recipes from API
    suspend fun getRecipesFromApi(): List<RecipeModel> {
        return try {
            val response = apiClient.getRecipes()
            response.map { it.toModel() } // Map DTOs to RecipeModel
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching recipes from API: ${e.message}")
            emptyList() // Return empty list in case of failure
        }
    }

    // Fetch recipe details by ID from API
    suspend fun getRecipeDetailsFromApi(id: Int): RecipeModel? {
        return try {
            val response = apiClient.getRecipeDetails(id)
            response.toModel() // Map DTO to RecipeModel
        } catch (e: Exception) {
            Log.e("Repository", "Error fetching recipe details from API: ${e.message}")
            null
        }
    }

    // Original JSON functions (retain these for local JSON handling)
    suspend fun getAllRecipes(): List<RecipeModel> {
        return readRecipesFromJson().map { it.toModel() }
    }

    // Toggle favorite status for a recipe
    suspend fun toggleFavorite(recipe: RecipeModel) {
        val updatedRecipe = recipe.copy(isFavorite = !recipe.isFavorite)
        val recipeJson = gson.toJson(updatedRecipe)
        recipeDao.updateRecipe(recipe.id.toLong(), recipeJson)
    }

    // Get all favorite recipes
    suspend fun getFavoriteRecipes(): List<RecipeModel> {
        return getAllLocalRecipes().filter { it.isFavorite }
    }

    private fun readRecipesFromJson(): List<RecipeDTO> {
        try {
            val jsonString = context.assets
                .open("recipes.json")
                .bufferedReader()
                .use { it.readText() }

            data class RecipeResponse(val recipes: List<RecipeDTO>)
            val response = gson.fromJson(jsonString, RecipeResponse::class.java)
            Log.d("Repository", "Successfully loaded ${response.recipes.size} recipes")
            return response.recipes

        } catch (e: IOException) {
            Log.e("Repository", "Error reading recipes file: ${e.message}")
            return emptyList()
        } catch (e: Exception) {
            Log.e("Repository", "Error parsing recipes: ${e.message}")
            return emptyList()
        }
    }

    // Room database functions
    suspend fun insertRecipe(recipe: RecipeModel) {
        try {
            val recipeJson = gson.toJson(recipe)
            val entity = RecipeEntity(json = recipeJson)
            recipeDao.insertRecipe(entity)
            Log.d("Repository", "Recipe inserted successfully")
        } catch (e: Exception) {
            Log.e("Repository", "Error inserting recipe: ${e.message}")
            throw e
        }
    }

    suspend fun getAllLocalRecipes(): List<RecipeModel> {
        return try {
            recipeDao.getAllRecipes().map { entity ->
                try {
                    val recipe = gson.fromJson(entity.json, RecipeModel::class.java)
                    recipe.copy(id = entity.internalId.toInt())
                } catch (e: Exception) {
                    Log.e("Repository", "Error parsing recipe JSON for id ${entity.internalId}: ${e.message}")
                    throw e
                }
            }
        } catch (e: Exception) {
            Log.e("Repository", "Error getting all local recipes: ${e.message}")
            throw e
        }
    }

    suspend fun getLocalRecipeById(id: Long): RecipeModel? {
        return recipeDao.getRecipeById(id)?.let { entity ->
            val jsonObject = JSONObject(entity.json)
            jsonObject.put("id", entity.internalId)
            gson.fromJson(jsonObject.toString(), RecipeDTO::class.java).toModel()
        }
    }

    suspend fun deleteRecipe(recipe: RecipeModel) {
        val recipeJson = gson.toJson(recipe)
        recipeDao.deleteRecipe(RecipeEntity(recipe.id.toLong(), recipeJson))
    }

    // Mapping functions
    private fun RecipeDTO.toModel(): RecipeModel {
        return RecipeModel(
            id = this.recipeID,
            name = this.name,
            description = this.description,
            thumbnailUrl = this.thumbnailUrl,
            servings = this.numServings,
            components = this.components.mapToComponentModels(),
            instructions = this.instructions.mapToInstructionModels(),
            nutrition = this.nutrition.toModel(),
            isFavorite = this.isFavorite
        )
    }

    private fun List<ComponentDTO>.mapToComponentModels(): List<ComponentModel> {
        return this.map { it.toModel() }
    }

    private fun ComponentDTO.toModel(): ComponentModel {
        return ComponentModel(
            rawText = this.rawText,
            ingredient = this.ingredient.name,
            amount = "${this.measurement.quantity} ${this.measurement.unit.abbreviation}",
            position = this.position
        )
    }

    private fun List<InstructionDTO>.mapToInstructionModels(): List<InstructionModel> {
        return this.map { it.toModel() }
    }

    private fun InstructionDTO.toModel(): InstructionModel {
        return InstructionModel(
            id = this.instructionID,
            text = this.displayText,
            position = this.position
        )
    }

    private fun NutritionDTO.toModel(): NutritionModel {
        return NutritionModel(
            calories = this.calories,
            protein = this.protein,
            fat = this.fat,
            carbohydrates = this.carbohydrates
        )
    }
}
