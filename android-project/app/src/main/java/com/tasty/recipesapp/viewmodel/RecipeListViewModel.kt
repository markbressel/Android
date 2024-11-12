package com.tasty.recipesapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.Respository.RecipeRepository
import com.tasty.recipesapp.models.RecipeModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipeRepository(application)

    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> = _recipes

    private val _randomRecipe = MutableLiveData<RecipeModel>()
    val randomRecipe: LiveData<RecipeModel> = _randomRecipe

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    //for selected recipe
    private val _selectedRecipe = MutableLiveData<RecipeModel>()
    val selectedRecipe: LiveData<RecipeModel> = _selectedRecipe

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val recipeList = withContext(Dispatchers.IO) {
                    repository.getAllRecipes()
                }
                _recipes.value = recipeList
            } catch (e: Exception) {
                _error.value = "Failed to load recipes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    //fun to get recipe by id
    fun getRecipeById(recipeId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Find recipe from current list first
                _recipes.value?.find { it.id == recipeId }?.let { recipe ->
                    _selectedRecipe.value = recipe
                    return@launch
                }

                // If not found in current list, load from repository
                val recipeList = withContext(Dispatchers.IO) {
                    repository.getAllRecipes()
                }

                recipeList.find { it.id == recipeId }?.let { recipe ->
                    _selectedRecipe.value = recipe
                } ?: run {
                    _error.value = "Recipe not found"
                }

            } catch (e: Exception) {
                _error.value = "Failed to load recipe: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}