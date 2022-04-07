package ru.ama.ottest.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.ama.ottest.R
import ru.ama.ottest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            launchFirstScreen()
        }
    }

    private fun launchFirstScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, WelcomeFragment.newInstance())
            .commit()
    }
}
