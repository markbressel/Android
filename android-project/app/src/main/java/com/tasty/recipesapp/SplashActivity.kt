package com.tasty.recipesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tasty.recipesapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    override fun onStart() {
        Log.i("SplashActivity", "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.i("SplashActivity", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.i("SplashActivity", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.i("SplashActivity", "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i("SplashActivity", "onDestroy")
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("SplashActivity", "onCreate")
        enableEdgeToEdge()
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener{
            Log.i("SplashActivity", "startButton clicked")
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("message", "Hello from Splash!")
            startActivity(intent)
            finish()
        }
        //setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}