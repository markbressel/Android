package com.tasty.recipesapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipeRepository(
        application,
        RecipeDatabase.getDatabase(application).recipeDao()
    )

    // LiveData for recipes from API
    private val _recipes = MutableLiveData<List<RecipeModel>>()
    val recipes: LiveData<List<RecipeModel>> get() = _recipes

    // LiveData for user's local recipes
    private val _localRecipes = MutableLiveData<List<RecipeModel>>()
    val localRecipes: LiveData<List<RecipeModel>> get() = _localRecipes

    // Random recipe
    private val _randomRecipe = MutableLiveData<RecipeModel>()
    val randomRecipe: LiveData<RecipeModel> get() = _randomRecipe

    // Selected recipe
    private val _selectedRecipe = MutableLiveData<RecipeModel>()
    val selectedRecipe: LiveData<RecipeModel> get() = _selectedRecipe

    // Loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Error state
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        loadRecipesFromApi() // Load recipes from the API on initialization
        loadLocalRecipes() // Load recipes stored locally
    }

    // Load recipes from API
    private fun loadRecipesFromApi() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val recipeList = withContext(Dispatchers.IO) {
                    repository.getRecipesFromApi()
                }
                Log.d("RecipeListViewModel", "Loaded ${recipeList.size} recipes from API")
                _recipes.value = recipeList
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Error loading recipes from API", e)
                _error.value = "Failed to load recipes from API: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Load recipes stored locally
    private fun loadLocalRecipes() {
        viewModelScope.launch {
            try {
                val recipes = withContext(Dispatchers.IO) {
                    repository.getAllLocalRecipes()
                }
                _localRecipes.value = recipes
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Error loading local recipes", e)
                _error.value = "Failed to load local recipes: ${e.message}"
            }
        }
    }

    // Get recipe details by ID from API or local storage
    fun getRecipeById(recipeId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Check local recipes first
                _localRecipes.value?.find { it.id == recipeId }?.let { recipe ->
                    _selectedRecipe.value = recipe
                    return@launch
                }

                // Then check API recipes
                _recipes.value?.find { it.id == recipeId }?.let { recipe ->
                    _selectedRecipe.value = recipe
                    return@launch
                }

                // If not found, try fetching details from the API
                val recipeDetail = withContext(Dispatchers.IO) {
                    repository.getRecipeDetailsFromApi(recipeId)
                }

                recipeDetail?.let {
                    _selectedRecipe.value = it
                } ?: run {
                    _error.value = "Recipe not found"
                }

            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Error fetching recipe details", e)
                _error.value = "Failed to fetch recipe details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Fetch a random recipe from the current API list
    fun getRandomRecipe() {
        viewModelScope.launch {
            _recipes.value?.let { recipeList ->
                if (recipeList.isNotEmpty()) {
                    _randomRecipe.value = recipeList.random()
                } else {
                    _error.value = "No recipes available to pick a random one."
                }
            }
        }
    }

    // Toggle favorite status for a recipe
    fun toggleFavorite(recipe: RecipeModel) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.toggleFavorite(recipe)
                }
                loadLocalRecipes() // Refresh local recipes
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "Error toggling favorite", e)
                _error.value = "Failed to update favorite status: ${e.message}"
            }
        }
    }
}
