package com.tasty.recipesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecipeRepository(
        application,
        RecipeDatabase.getDatabase(application).recipeDao()
    )

    private val _myRecipes = MutableLiveData<List<RecipeModel>>()
    val myRecipes: LiveData<List<RecipeModel>> = _myRecipes

    private val _favorites = MutableLiveData<List<RecipeModel>>()
    val favorites: LiveData<List<RecipeModel>> = _favorites

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _selectedRecipe = MutableLiveData<RecipeModel?>()
    val selectedRecipe: LiveData<RecipeModel?> = _selectedRecipe

    init {
        loadMyRecipes()
        loadFavorites()
    }

    // Add toggle favorite function
    fun toggleFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(recipe)
                loadMyRecipes()  // Reload to update the UI
                loadFavorites()  // Reload favorites
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to update favorite: ${e.message}"
            }
        }
    }

    // Add load favorites function
    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                _favorites.value = repository.getFavoriteRecipes()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load favorites: ${e.message}"
            }
        }
    }

    private fun loadMyRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _myRecipes.value = repository.getAllLocalRecipes()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load recipes: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRecipeById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // First try to find in the current list
                _myRecipes.value?.find { it.id == id }?.let {
                    _selectedRecipe.value = it
                    return@launch
                }

                // If not found in current list, try to load from repository
                val recipe = repository.getLocalRecipeById(id.toLong())
                if (recipe != null) {
                    _selectedRecipe.value = recipe
                } else {
                    _error.value = "Recipe not found"
                }
            } catch (e: Exception) {
                _error.value = "Failed to load recipe: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addRecipe(recipe: RecipeModel) {
        viewModelScope.launch {
            try {
                repository.insertRecipe(recipe)
                loadMyRecipes() // Reload the list
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to add recipe: ${e.message}"
            }
        }
    }

    fun deleteRecipe(recipe: RecipeModel) {
        viewModelScope.launch {
            try {
                repository.deleteRecipe(recipe)
                loadMyRecipes() // Reload the list
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to delete recipe: ${e.message}"
            }
        }
    }
}