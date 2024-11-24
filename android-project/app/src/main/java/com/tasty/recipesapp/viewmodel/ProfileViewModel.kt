package com.tasty.recipesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: RecipeRepository) : ViewModel() {
    // Fetch all recipes from the database as RecipeModel
    fun getAllRecipes(callback: (List<RecipeModel>) -> Unit) {
        viewModelScope.launch {
            val recipes = repository.getAllLocalRecipes() // Returns List<RecipeModel>
            callback(recipes)
        }
    }

    // Insert a recipe into the database as RecipeModel
    fun insertRecipe(recipe: RecipeModel) {
        viewModelScope.launch {
            repository.insertRecipe(recipe) // Accepts RecipeModel
        }
    }

    // Delete a recipe from the database as RecipeModel
    fun deleteRecipe(recipe: RecipeModel) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe) // Accepts RecipeModel
        }
    }
}
