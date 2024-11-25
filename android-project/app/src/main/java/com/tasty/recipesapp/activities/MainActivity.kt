package com.tasty.recipesapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tasty.recipesapp.R
import com.tasty.recipesapp.ui.home.HomeFragment
import com.tasty.recipesapp.ui.profile.ProfileFragment
import com.tasty.recipesapp.ui.recipe.RecipesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var menuTitle: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuTitle = findViewById(R.id.menuTitle)
        menuTitle.text = "Home"
        menuTitle.setTextColor(android.graphics.Color.WHITE)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setupWithNavController(bottomNavigationView, navController)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    updateMenuTitle("Home")
                    //loadFragment(HomeFragment())
                    navController.navigate(R.id.homeFragment)
                }
                R.id.recipesFragment -> {
                    updateMenuTitle("Recipes")
                    //loadFragment(RecipesFragment())
                    navController.navigate(R.id.recipesFragment)
                }
                R.id.profileFragment -> {
                    updateMenuTitle("Profile")
                    //loadFragment(ProfileFragment())
                    navController.navigate(R.id.profileFragment)
                }
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun updateMenuTitle(title: String) {
        menuTitle.text = title
        menuTitle.setTextColor(android.graphics.Color.WHITE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeFragment -> {
                updateMenuTitle("Home")
                loadFragment(HomeFragment())
            }
            R.id.recipesFragment -> {
                updateMenuTitle("Recipes")
                loadFragment(RecipesFragment())
            }
            R.id.profileFragment -> {
                updateMenuTitle("Profile")
                loadFragment(ProfileFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
