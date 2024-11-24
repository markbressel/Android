package com.tasty.recipesapp.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tasty.recipesapp.R
import com.tasty.recipesapp.database.RecipeDatabase
import com.tasty.recipesapp.models.ComponentModel
import com.tasty.recipesapp.models.InstructionModel
import com.tasty.recipesapp.models.NutritionModel
import com.tasty.recipesapp.models.RecipeModel
import com.tasty.recipesapp.repository.RecipeRepository
import com.tasty.recipesapp.viewmodel.ProfileViewModel
import com.tasty.recipesapp.viewmodel.ProfileViewModelFactory

class NewRecipeFragment : Fragment() {

    private val repository: RecipeRepository by lazy {
        val database = RecipeDatabase.getDatabase(requireContext())
        RecipeRepository(requireContext(), database.recipeDao())
    }

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, ProfileViewModelFactory(repository))[ProfileViewModel::class.java]
    }

    private lateinit var ingredientLayout: LinearLayout
    private lateinit var instructionLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_new_recipe, container, false)

        ingredientLayout = root.findViewById(R.id.ingredient_container)
        instructionLayout = root.findViewById(R.id.instruction_container)

        // "Add Component" gomb megnyomásakor három mező jelenik meg
        root.findViewById<FloatingActionButton>(R.id.add_component_button).setOnClickListener {
            addIngredientFields(ingredientLayout)
        }

        // "Add Instruction" gomb megnyomásakor csak egy mező jelenik meg
        root.findViewById<FloatingActionButton>(R.id.add_instruction_button).setOnClickListener {
            addInstructionField(instructionLayout)
        }

        // "Save Recipe" gomb működése
        root.findViewById<FloatingActionButton>(R.id.save_recipe_button).setOnClickListener {
            saveRecipe()
        }

        return root
    }

    // Ez a metódus három mezőt ad hozzá az Ingredient szekcióhoz
    private fun addIngredientFields(container: LinearLayout) {
        val ingredientInput = createEditText("Ingredient")
        val quantityInput = createEditText("Quantity")
        val unitInput = createEditText("Unit")

        val inputLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(ingredientInput)
            addView(quantityInput)
            addView(unitInput)
        }

        container.addView(inputLayout)
    }

    // Ez a metódus csak egy mezőt ad hozzá az Instruction szekcióhoz
    private fun addInstructionField(container: LinearLayout) {
        val instructionInput = createEditText("Instruction")

        val inputLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(instructionInput)
        }

        container.addView(inputLayout)
    }

    // Segédfüggvény, amely létrehoz egy EditText mezőt a kívánt hint-tel
    private fun createEditText(hint: String): EditText {
        return EditText(requireContext()).apply {
            this.hint = hint
            this.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    private fun saveRecipe() {
        val name = view?.findViewById<EditText>(R.id.recipe_name_input)?.text.toString()
        val description = view?.findViewById<EditText>(R.id.short_description_input)?.text.toString()

        val ingredients = (0 until ingredientLayout.childCount).map { index ->
            val inputLayout = ingredientLayout.getChildAt(index) as LinearLayout
            val ingredientText = (inputLayout.getChildAt(0) as EditText).text.toString()
            val quantityText = (inputLayout.getChildAt(1) as EditText).text.toString()
            val unitText = (inputLayout.getChildAt(2) as EditText).text.toString()

            // Ha szükséges, igazítsd az objektumot
            val rawText = "$ingredientText $quantityText $unitText"

            ComponentModel(
                rawText = rawText,
                ingredient = ingredientText,
                amount = quantityText,
                position = index + 1
            )
        }.filter { it.ingredient.isNotBlank() }

        val instructions = (0 until instructionLayout.childCount).map { index ->
            val inputField = instructionLayout.getChildAt(index) as LinearLayout
            val instructionText = (inputField.getChildAt(0) as EditText).text.toString()
            InstructionModel(
                id = index + 1,
                text = instructionText,
                position = index + 1
            )
        }.filter { it.text.isNotBlank() }

        if (name.isBlank() || description.isBlank()) {
            Toast.makeText(requireContext(), "Name and description are required!", Toast.LENGTH_SHORT).show()
            return
        }

        val recipe = RecipeModel(
            id = 0,
            name = name,
            description = description,
            thumbnailUrl = "",
            servings = 4,
            components = ingredients,
            instructions = instructions,
            nutrition = NutritionModel(0, 0, 0, 0)
        )

        // Ez a sor hívja meg a viewModelt, hogy elmentse a receptet
        viewModel.insertRecipe(recipe)
        Toast.makeText(requireContext(), "Recipe saved!", Toast.LENGTH_SHORT).show()
    }
}
