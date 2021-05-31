package com.example.instagramclone_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instagramclone_android.databinding.ActivityAccountSettingsBinding

class AccountSettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivityAccountSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}