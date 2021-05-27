package com.example.instagramclone_android

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.instagramclone_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                textView.setText("home")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_search -> {
                textView.setText("search")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_add_post -> {
                textView.setText("app_post")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {
                textView.setText("notifications")
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_profile -> {
                textView.setText("profile")
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        textView = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

    private fun moveToFragment(fragment: Fragment)
    {
//        val fragmentTrans = supportFragmentManager.beginTransaction()
//        fragmentTrans.replace(R.id.fragment_container, fragment)
//        fragmentTrans.commit()
    }
}