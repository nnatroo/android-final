package com.example.natroshvili_hm3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {
    private lateinit var buttonChangePassword: Button
    private lateinit var editTextPassword: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        passwordChangeListeners()
    }

    private fun init() {
        buttonChangePassword = requireView().findViewById(R.id.buttonChangePassword)
        editTextPassword = requireView().findViewById(R.id.editTextPassword)
    }

    private fun passwordChangeListeners() {
        buttonChangePassword.setOnClickListener() {

            val passwordChange = editTextPassword.text.toString()

            if (passwordChange.isEmpty()) {
                editTextPassword.error = "Password is empty!"
            }
            if (!(passwordChange.matches(".*[A-Z].*".toRegex())) &&
                !(passwordChange.matches(".*[a-z].*".toRegex()))
            ) {
                editTextPassword.error = "Must contain letters"
                return@setOnClickListener
            }
            if (!(passwordChange.matches(".*[0-9].*".toRegex()))) {
                editTextPassword.error = "Must contain digits"
                return@setOnClickListener
            }
            if (!(passwordChange.matches(".*[$@#!?_].*".toRegex()))) {
                editTextPassword.error = "Must contain special symbols '$@#!?_'"
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().currentUser?.updatePassword(passwordChange)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Password Changed !", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                            .replace(R.id.nav_host_fragment_activity_Home, ProfileFragment())
                            .commit()
                    } else {
                        Toast.makeText(activity, "Error !", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}