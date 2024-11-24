package com.tasty.recipesapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tasty.recipesapp.R
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.repository.RecipeRepository

class ProfileFragment : Fragment() {

    private lateinit var repository: RecipeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = requireContext()

        // Initialize the RecipeDao through RecipeDatabase
        val recipeDao = RecipeDatabase.getDatabase(context).recipeDao()

        // Initialize the repository with both context and recipeDao
        repository = RecipeRepository(context, recipeDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        // Example usage of the repository
        // repository.getAllLocalRecipes()

        return root
    }
}
