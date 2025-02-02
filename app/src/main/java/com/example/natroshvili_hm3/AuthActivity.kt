package com.example.natroshvili_hm3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragmentContainerView, RegisterFragment())
//                .commit()
//        }
    }
}