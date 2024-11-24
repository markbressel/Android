package com.tasty.recipesapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.databinding.ItemRecipeBinding
import com.tasty.recipesapp.models.RecipeModel

class RecipeAdapter(
    private var recipes: List<RecipeModel> = emptyList(),
    private val onItemClick: (RecipeModel) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecipes(newRecipes: List<RecipeModel>) {
        recipes = newRecipes
        notifyDataSetChanged()  // A lista frissítése után értesíteni kell az adaptert
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        Log.d("RecipeAdapter", "onCreateViewHolder")
        // Az inflálás javítása
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        Log.d("RecipeAdapter", "onBindViewHolder")
        holder.bind(recipes[position])  // A recept hozzárendelése a ViewHolderhez
    }

    override fun getItemCount(): Int = recipes.size

    class RecipeViewHolder(
        private val binding: ItemRecipeBinding,
        private val onItemClick: (RecipeModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeModel) {
            binding.apply {
                recipeName.text = recipe.name  // A recept neve
                recipeDescription.text = recipe.description  // A recept leírása
                servingsInfo.text = "Serves ${recipe.servings}"  // Adott recept adagszáma
                calories.text = "${recipe.nutrition.calories} calories"  // Kalória információ

                // Kattintás esemény beállítása
                root.setOnClickListener { onItemClick(recipe) }
            }
        }
    }
}
