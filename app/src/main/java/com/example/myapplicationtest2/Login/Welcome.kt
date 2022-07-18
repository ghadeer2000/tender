package com.example.myapplicationtest2.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.SellerLogin.SignInSeller
import com.example.myapplicationtest2.SellerLogin.SignUpSeller
import kotlinx.android.synthetic.main.activity_welcome.*

class Welcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btn_signUp.setOnClickListener {
            val i = Intent(this,SignUpCraft::class.java)
            startActivity(i)

        }
        btn_signIn.setOnClickListener {
            val i = Intent(this,SignInSeller::class.java)
            startActivity(i)

        }

    }
}