package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tasty.recipesapp.R
import com.tasty.recipesapp.adapter.RecipeAdapter
import com.tasty.recipesapp.databinding.FragmentRecipesBinding
import com.tasty.recipesapp.viewmodel.RecipeListViewModel

class RecipesFragment : Fragment() {

    private val viewModel: RecipeListViewModel by activityViewModels()
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabAddRecipe = view.findViewById<FloatingActionButton>(R.id.fab_add_recipe)

        // Kattintási esemény beállítása
        fabAddRecipe.setOnClickListener {
            // Navigálás a RecipesFragment-ből a NewRecipeFragment-re
            findNavController().navigate(R.id.action_recipesFragment_to_newRecipeFragment)
        }

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipe ->
            // Navigálás a recept részletekhez
            val directions = RecipesFragmentDirections
                .actionRecipesFragmentToRecipeDetailFragment(recipeId = recipe.id)
            findNavController().navigate(directions)
        }

        binding.recyclerView.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        // Az összes recept figyelése
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.updateRecipes(recipes)  // Frissíti az adaptert az új listával
        }

        // Betöltési indikátor figyelése
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Hibák kezelése
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
