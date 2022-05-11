package ru.ama.ottest.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.ama.ottest.R
import ru.ama.ottest.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

   // private lateinit var binding: ActivityMainBinding
  //  private lateinit var viewModel: TestsViewModel

 //   @Inject
  //  lateinit var viewModelFactory: ViewModelFactory

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

   /* private val component by lazy {
        (application as MyApplication).component
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
    //    component.inject(this)
        super.onCreate(savedInstanceState)
     //   binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            launchFirstScreen()
        }
      /*  try {

            viewModel = ViewModelProvider(this, viewModelFactory)[TestsViewModel::class.java]
        
        }catch (e:Exception){}*/

    }

    private fun launchFirstScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, WelcomeFragment.newInstance())
            .commit()
    }
}
