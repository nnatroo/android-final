package com.example.natroshvili_hm3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragmentContainerView, RegisterFragment())
//                .commit()
//        }
    }
}