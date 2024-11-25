package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasty.recipesapp.R
import com.tasty.recipesapp.adapter.RecipeAdapter
import com.tasty.recipesapp.databinding.FragmentRecipesBinding
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.viewmodel.ProfileViewModel

class RecipesFragment : Fragment() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            _binding = FragmentRecipesBinding.inflate(inflater, container, false)
            return binding.root
        } catch (e: Exception) {
            Log.e("RecipesFragment", "Error in onCreateView", e)
            throw e
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            setupRecyclerView()
            observeViewModel()
            binding.addRecipeButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_newRecipeFragment)
            }
        } catch (e: Exception) {
            Log.e("RecipesFragment", "Error in onViewCreated", e)
            Toast.makeText(context, "Error loading recipes: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter(
            onItemClick = { recipe ->
                val directions = RecipesFragmentDirections
                    .actionRecipesFragmentToRecipeDetailFragment(recipeId = recipe.id)
                findNavController().navigate(directions)
            },
            onFavoriteClick = { recipe ->
                viewModel.toggleFavorite(recipe)
            },
            onItemLongClick = { recipe ->
                showDeleteConfirmationDialog(recipe)
            }
        )

        binding.recyclerView.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeViewModel() {
        viewModel.myRecipes.observe(viewLifecycleOwner) { recipes ->
            recipeAdapter.updateRecipes(recipes)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Log.e("RecipesFragment", "Error occurred: $it")
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(recipe: RecipeModel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Recept törlése")
            .setMessage("Biztosan törölni szeretnéd a(z) ${recipe.name} receptet?")
            .setPositiveButton("Igen") { _, _ ->
                viewModel.deleteRecipe(recipe)
                Toast.makeText(context, "Recept törölve", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Mégse", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
