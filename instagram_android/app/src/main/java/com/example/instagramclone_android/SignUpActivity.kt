package com.example.instagramclone_android

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.instagramclone_android.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signinLinkBtn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding.registerBtn.setOnClickListener {
            CreateAccount()
        }

    }

    private fun CreateAccount() {
        val fullName = binding.fullnameSignin.text.toString()
        val userName = binding.usernameSignin.text.toString()
        val email = binding.emailSignin.text.toString()
        val password = binding.passwordSignin.text.toString()

        when {

            TextUtils.isEmpty(fullName) -> Toast.makeText(this, "full name is required", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(userName) -> Toast.makeText(this, "user name is required", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(email) -> Toast.makeText(this, "email is required", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this, "password is required", Toast.LENGTH_LONG).show()


            else -> {

                val progressDialog = ProgressDialog(this@SignUpActivity)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Please Wait, this may take a while....")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserInfo(fullName, userName, email, progressDialog)
                    } else {
                        val message = task.exception!!.toString()
                        Toast.makeText(this, "Error : $message", Toast.LENGTH_LONG).show()
                        mAuth.signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun saveUserInfo(fullName: String, userName: String, email: String, progressDialog: ProgressDialog) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = fullName
        userMap["username"] = userName
        userMap["email"] = email
        userMap["bio"] = "this is an instagram clone app"
        userMap["image"] = "https://firebasestorage.googleapis.com/v0/b/instagramapp-c04e1.appspot.com/o/Defaults%20Images%2Fprofile.jpeg?alt=media&token=f217e5a6-2f1c-4f26-b9b4-2304b7c3eb5d"

        userRef.child(currentUserID).setValue(userMap).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                progressDialog.dismiss()
                Toast.makeText(this, "회원가입에 성공하였습니다", Toast.LENGTH_LONG).show()
                val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                val message = task.exception!!.toString()
                Toast.makeText(this, "Error : $message", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                progressDialog.dismiss()
            }
        }
    }
}