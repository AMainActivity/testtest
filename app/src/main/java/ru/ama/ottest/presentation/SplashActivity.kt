package ru.ama.ottest.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (application as MyApplication).component
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.canStart.collect {
                    if (it)
                    {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
            /*viewModel.canStart.observe(this) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }*/
        }

    }
