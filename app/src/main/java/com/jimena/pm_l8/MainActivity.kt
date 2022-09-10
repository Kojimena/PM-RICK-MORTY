package com.jimena.pm_l8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jimena.pm_l8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var toolbar: MaterialToolbar
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //configurar toolbar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_MainActivity) as NavHostFragment
        navController = navHostFragment.navController

        val appbarConfig = AppBarConfiguration(navController.graph)
        toolbar = findViewById(R.id.toolbar_MainActivity)
        toolbar.setupWithNavController(navController, appbarConfig)


        setNavigation()

    }

    private fun setNavigation() {
        toolbar.visibility = View.VISIBLE

    }

}