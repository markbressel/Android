package com.tasty.recipesapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        loadFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
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
