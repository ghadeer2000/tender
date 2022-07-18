package com.example.myapplicationtest2.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.SellerLogin.SignUpSeller
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_verification.*

class Verification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        btn_check.setOnClickListener {
            val i = Intent(this, newPassword::class.java)
            startActivity(i)

        }
    }
}