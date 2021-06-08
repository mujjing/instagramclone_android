package com.example.instagramclone_android.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagramclone_android.AccountSettingsActivity
import com.example.instagramclone_android.Model.User
import com.example.instagramclone_android.R
import com.example.instagramclone_android.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentProfileBinding
    private lateinit var profileId: String
    private lateinit var firebaseUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid

        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref != null) {
            this.profileId = pref.getString("profileId", "none").toString()
        }

        if (profileId == firebaseUser) {
            binding.editAccountSettingsBtn.text = "Edit Profile"
        } else if (profileId != firebaseUser) {
            checkFollowAndFollowingButtonStatus()
        }

        binding.editAccountSettingsBtn.setOnClickListener {

            val getButtonText = binding.editAccountSettingsBtn.text.toString()
            when {
                getButtonText == "Edit Profile" -> startActivity(Intent(context, AccountSettingsActivity::class.java))
                getButtonText == "Follow" -> {
                    firebaseUser.let { itl ->
                        FirebaseDatabase.getInstance().reference
                                .child("Follow").child(itl.toString())
                                .child("Following").child(profileId).setValue(true)
                    }
                    firebaseUser.let { itl ->
                        FirebaseDatabase.getInstance().reference
                                .child("Follow").child(profileId)
                                .child("Followers").child(itl.toString()).setValue(true)
                    }
                }
                getButtonText == "Following" -> {
                    firebaseUser.let { itl ->
                        FirebaseDatabase.getInstance().reference
                                .child("Follow").child(itl.toString())
                                .child("Following").child(profileId).removeValue()
                    }
                    firebaseUser.let { itl ->
                        FirebaseDatabase.getInstance().reference
                                .child("Follow").child(profileId)
                                .child("Followers").child(itl.toString()).removeValue()
                    }
                }
            }
        }
        getFollowers()
        getFollowing()
        userInfo()

        return binding.root
    }

    private fun checkFollowAndFollowingButtonStatus() {
        var firebaseUid = FirebaseAuth.getInstance().currentUser!!.uid
        val followingRef = firebaseUid.let {
            itl ->
            FirebaseDatabase.getInstance().reference
                    .child("Follow").child(itl.toString())
                    .child("Following")
        }
        if (followingRef != null) {
            followingRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(profileId).exists()) {
                        binding.editAccountSettingsBtn.text = "Following"
                    } else {
                        binding.editAccountSettingsBtn.text = "Follow"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    private fun getFollowers() {
        val followersRef =
            FirebaseDatabase.getInstance().reference
                    .child("Follow").child(profileId)
                    .child("Followers")

        followersRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.totalFollowers.text = snapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getFollowing() {
        val followersRef = FirebaseDatabase.getInstance().reference
                    .child("Follow").child(profileId)
                    .child("Following")

        followersRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.totalFollowing.text = snapshot.childrenCount.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun userInfo(){
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(profileId)
        usersRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(binding.proImageProfileFrag)
                    binding.profileFragmentUsername.text = user!!.getUsername()
                    binding.fullNameProfileFrag.text = user!!.getFullname()
                    binding.bioProfileFrag.text = user!!.getBio()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onStop() {
        super.onStop()
        var firebaseUid = FirebaseAuth.getInstance().currentUser!!.uid
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUid)
        pref?.apply()

    }

    override fun onPause() {
        super.onPause()
        var firebaseUid = FirebaseAuth.getInstance().currentUser!!.uid
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUid)
        pref?.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        var firebaseUid = FirebaseAuth.getInstance().currentUser!!.uid
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUid)
        pref?.apply()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}