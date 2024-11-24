package com.tasty.recipesapp.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.tasty.recipesapp.database.RecipeDao
import com.tasty.recipesapp.database.RecipeEntity
import com.tasty.recipesapp.dto.ComponentDTO
import com.tasty.recipesapp.dto.InstructionDTO
import com.tasty.recipesapp.dto.NutritionDTO
import com.tasty.recipesapp.dto.RecipeDTO
import com.tasty.recipesapp.models.ComponentModel
import com.tasty.recipesapp.models.InstructionModel
import com.tasty.recipesapp.models.NutritionModel
import com.tasty.recipesapp.models.RecipeModel
import org.json.JSONObject

import java.io.IOException

class RecipeRepository(
    private val context: Context,
    private val recipeDao: RecipeDao
) {
    private val gson = Gson()

    // Original JSON functions
    suspend fun getAllRecipes(): List<RecipeModel> {
        return readRecipesFromJson().map { it.toModel() }
    }

    private fun readRecipesFromJson(): List<RecipeDTO> {
        try {
            val jsonString = context.assets
                .open("recipes.json")
                .bufferedReader()
                .use { it.readText() }

            data class RecipeResponse(val recipes: List<RecipeDTO>)
            val response = gson.fromJson(jsonString, RecipeResponse::class.java)
            Log.d("JSON Response", response.toString());
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

    // New Room database functions
    suspend fun insertRecipe(recipe: RecipeModel) {
        val recipeJson = gson.toJson(recipe)
        val entity = RecipeEntity(json = recipeJson)
        recipeDao.insertRecipe(entity)
    }

    suspend fun getAllLocalRecipes(): List<RecipeModel> {
        return recipeDao.getAllRecipes().map { entity ->
            val jsonObject = JSONObject(entity.json)
            Log.d("JSON Response", jsonObject.toString());
            jsonObject.put("id", entity.internalId)
            gson.fromJson(jsonObject.toString(), RecipeDTO::class.java).toModel()
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

    // Your existing mapping functions
    private fun RecipeDTO.toModel(): RecipeModel {
        return RecipeModel(
            id = this.recipeID,
            name = this.name,
            description = this.description,
            thumbnailUrl = this.thumbnailUrl,
            servings = this.numServings,
            components = this.components.mapToComponentModels(),
            instructions = this.instructions.mapToInstructionModels(),
            nutrition = this.nutrition.toModel()
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
