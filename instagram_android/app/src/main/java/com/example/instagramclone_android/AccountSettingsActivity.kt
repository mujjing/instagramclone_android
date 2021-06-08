package com.example.instagramclone_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instagramclone_android.Model.User
import com.example.instagramclone_android.databinding.ActivityAccountSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class AccountSettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivityAccountSettingsBinding
    private lateinit var firebaseUser: String
    private var checker = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid

        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@AccountSettingsActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        binding.saveProfileBtn.setOnClickListener {
            if (checker == "clicked") {

            } else {
                updateUserInfoOnly()
            }
        }

        userInfo()

        binding.closeProfileBtn.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun updateUserInfoOnly() {

        if (binding.fullNameProfileFrag.text.toString() == "") {
            Toast.makeText(this, "Please write full name first", Toast.LENGTH_SHORT).show()
        } else if (binding.usernameProfileFrag.text.toString() == "") {
            Toast.makeText(this, "Please write user name first", Toast.LENGTH_SHORT).show()
        } else if (binding.bioProfileFrag.text.toString() == "") {
            Toast.makeText(this, "Please write your bio first", Toast.LENGTH_SHORT).show()
        } else {
            firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid

            val usersRef = FirebaseDatabase.getInstance().reference.child("Users")

            val userMap = HashMap<String, Any>()
            userMap["fullname"] = binding.fullNameProfileFrag.text.toString().toLowerCase()
            userMap["username"] = binding.usernameProfileFrag.text.toString().toLowerCase()
            userMap["bio"] = binding.bioProfileFrag.text.toString().toLowerCase()

            usersRef.child(firebaseUser).updateChildren(userMap)

            Toast.makeText(this, "회원 정보를 수정 하였습니다", Toast.LENGTH_LONG).show()
            val intent = Intent(this@AccountSettingsActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun userInfo(){
        firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser)
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(binding.profileImageViewProrofile)
                    binding.usernameProfileFrag.setText(user!!.getUsername())
                    binding.fullNameProfileFrag.setText(user!!.getFullname())
                    binding.bioProfileFrag.setText(user!!.getBio())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}