package com.tasty.recipesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tasty.recipesapp.R
import com.tasty.recipesapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        // A Get Started gombhoz hozzáadjuk a navigációs funkciót
        binding.getStartedButton.setOnClickListener {
            // Navigálás a RecipesFragmentre
            findNavController().navigate(R.id.action_homeFragment_to_recipesFragment)
        }
    }
}
