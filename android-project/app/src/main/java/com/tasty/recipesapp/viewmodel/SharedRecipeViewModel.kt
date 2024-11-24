package com.tasty.recipesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedRecipeViewModel : ViewModel() {
    private val _recipes = MutableLiveData<MutableList<RecipeListViewModel>>(mutableListOf())
    val recipes: LiveData<MutableList<RecipeListViewModel>> = _recipes

    fun addRecipe(recipe: RecipeListViewModel) {
        val currentList = _recipes.value ?: mutableListOf()
        currentList.add(recipe)
        _recipes.value = currentList
    }
}