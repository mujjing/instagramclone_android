package com.example.instagramclone_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instagramclone_android.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupLinkBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}