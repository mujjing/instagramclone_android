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
import com.example.instagramclone_android.Fragments.HomeFragment
import com.example.instagramclone_android.Fragments.NotificationFragment
import com.example.instagramclone_android.Fragments.ProfileFragment
import com.example.instagramclone_android.Fragments.SearchFragment
import com.example.instagramclone_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    internal var selectedFragment: Fragment? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {

                selectedFragment = HomeFragment()
            }
            R.id.nav_search -> {

                selectedFragment = SearchFragment()
            }
            R.id.nav_add_post -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notifications -> {

                selectedFragment = NotificationFragment()
            }
            R.id.nav_profile -> {

                selectedFragment = ProfileFragment()
            }
        }

        if (selectedFragment != null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                selectedFragment!!
            ).commit()
        }

        false
    }

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        moveToFragment(HomeFragment())
    }

    private fun moveToFragment(fragment: Fragment)
    {
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()
    }
}