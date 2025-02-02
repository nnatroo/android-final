package com.example.natroshvili_hm3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnGoToRegister = view.findViewById<TextView>(R.id.btnGoToRegister)
        val btnForgotPassword = view.findViewById<TextView>(R.id.buttonReset)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(requireContext(), HomeActivity::class.java))
                            requireActivity().finish()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Login Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        btnGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btnForgotPassword.setOnClickListener(){
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }
}