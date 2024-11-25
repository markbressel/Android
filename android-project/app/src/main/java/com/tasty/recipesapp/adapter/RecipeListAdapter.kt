package com.tasty.recipesapp.adapter

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.databinding.ItemRecipeBinding
import com.tasty.recipesapp.models.RecipeModel
import android.util.Base64

class RecipeAdapter(
    private var recipes: List<RecipeModel> = emptyList(),
    private val onItemClick: (RecipeModel) -> Unit,
    private val onFavoriteClick: (RecipeModel) -> Unit,
    private val onItemLongClick: (RecipeModel) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecipes(newRecipes: List<RecipeModel>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        Log.d("RecipeAdapter", "onCreateViewHolder")
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding, onItemClick, onFavoriteClick, onItemLongClick)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        Log.d("RecipeAdapter", "onBindViewHolder")
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    class RecipeViewHolder(
        private val binding: ItemRecipeBinding,
        private val onItemClick: (RecipeModel) -> Unit,
        private val onFavoriteClick: (RecipeModel) -> Unit,
        private val onItemLongClick: (RecipeModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeModel) {
            binding.apply {
                recipeName.text = recipe.name
                recipeDescription.text = recipe.description
                servingsInfo.text = "Serves ${recipe.servings}"
                calories.text = "${recipe.nutrition.calories} calories"

                favoriteButton.text = if (recipe.isFavorite) "Liked" else "Like"
                favoriteButton.setOnClickListener {
                    onFavoriteClick(recipe)
                }

                if (recipe.thumbnailUrl.isNotEmpty()) {
                    try {
                        val imageBytes = Base64.decode(recipe.thumbnailUrl, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        recipeImage.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Log.e("RecipeViewHolder", "Error loading image: ${e.message}")
                        recipeImage.setImageResource(android.R.drawable.ic_menu_gallery)
                    }
                } else {
                    recipeImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }

                root.setOnClickListener { onItemClick(recipe) }
                root.setOnLongClickListener {
                    onItemLongClick(recipe)
                    true
                }
            }
        }
    }
}
