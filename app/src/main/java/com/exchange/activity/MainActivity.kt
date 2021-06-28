package com.exchange.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.exchange.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        supportActionBar?.hide()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.navController)
        floatingButton = findViewById(R.id.btSell)
        floatingButton.imageTintList = ColorStateList.valueOf(Color.parseColor("#C2F1DB"))
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false
        bottomNavigationView.setupWithNavController(navController)



    }

    override fun onResume() {
        super.onResume()

        floatingButton = findViewById(R.id.btSell)
        floatingButton.setOnClickListener {
            val intent = Intent(applicationContext, SellActivity::class.java)
            startActivity(intent)
        }

    }

}