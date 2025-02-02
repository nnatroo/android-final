package com.example.natroshvili_hm3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var imageViewPicture: ImageView
    private lateinit var editTextName: EditText
    private lateinit var textViewMail: TextView
    private lateinit var signOut: Button
    private lateinit var textViewChangePassword: TextView
    private lateinit var buttonSave: Button
    private lateinit var editTextBio: EditText
    private lateinit var editTextTweet: EditText
    private lateinit var buttonTweet: Button

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("UserInfo")
    private val dbTweets = FirebaseDatabase.getInstance().getReference("Tweets")

    private fun init() {
        imageViewPicture = requireView().findViewById(R.id.imageViewPicture)
        textViewMail = requireView().findViewById(R.id.textViewMail)
        signOut = requireView().findViewById(R.id.signOut)
        textViewChangePassword = requireView().findViewById(R.id.textViewChangePassword)
        buttonSave = requireView().findViewById(R.id.buttonSave)
        editTextName = requireView().findViewById(R.id.editTextName)
        editTextBio = requireView().findViewById(R.id.editTextBio)
        editTextTweet = requireView().findViewById(R.id.editTextTweet)
        buttonTweet = requireView().findViewById(R.id.buttonTweet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        signOutListener()
        saveButtonListeners()
        tweetListener()

        val userMail = FirebaseAuth.getInstance().currentUser?.email
        textViewMail.text = userMail

        db.child(auth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userInfo: UserInfo = snapshot.getValue(UserInfo::class.java) ?: return

                if (userInfo.name.isNotEmpty()) {
                    editTextName.hint = userInfo.name
                }
                if (userInfo.bio.isNotEmpty()) {
                    editTextBio.hint = userInfo.bio
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "DBerror", Toast.LENGTH_SHORT).show()
            }
        })

        textViewChangePassword.setOnClickListener() {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
                .replace(R.id.nav_host_fragment_activity_Home, ChangePasswordFragment())
                .commit()
        }
    }

    private fun tweetListener() {
        buttonTweet.setOnClickListener {
            val text = editTextTweet.text.toString()
            db.child(auth.currentUser?.uid!!).get().addOnSuccessListener {
                if (it.exists()) {
                    val tweetMessage = editTextTweet.text.toString()
                    if (tweetMessage.isNotEmpty()) {
                        val id = dbTweets.push().key
                        dbTweets.child(id.toString()).child("userName")
                            .setValue(editTextName.hint.toString())
                        dbTweets.child(id.toString()).child("text").setValue(text)
                        Toast.makeText(activity, "The post is published", Toast.LENGTH_LONG).show()
                        parentFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                            .replace(R.id.nav_host_fragment_activity_Home, FeedFragment())
                            .commit()
                    } else Toast.makeText(activity, "Failed to post", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveButtonListeners() {
        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val bio = editTextBio.text.toString()
            val userInfo = UserInfo(name, bio)

            if (name.isEmpty()) {
                editTextName.error = "Enter Name"
            }

            editTextBio.hint = bio

            db.child(auth.currentUser?.uid!!).setValue(userInfo).addOnSuccessListener {
                Toast.makeText(activity, "Changes Saved Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed to save changes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signOutListener() {
        signOut.setOnClickListener() {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, AuthActivity::class.java))
            activity?.finish()
        }
    }
}