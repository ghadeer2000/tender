package com.example.myapplicationtest2.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.SellerLogin.SignUpSeller
import kotlinx.android.synthetic.main.activity_forget_password.*


class ForgetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        btn_send.setOnClickListener {
            val i = Intent(this, Verification::class.java)
            startActivity(i)

        }
    }
}