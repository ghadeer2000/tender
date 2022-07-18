package com.example.myapplicationtest2.Seller

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationtest2.R
import kotlinx.android.synthetic.main.activity_delivery.*
import kotlinx.android.synthetic.main.first_step_pay.*

class DeliveryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)

        btn_confirm.setOnClickListener {
            val i = Intent(this, DoneActivity::class.java)
            startActivity(i)
        }
        val prefs: SharedPreferences = this.getSharedPreferences(
            "sendpriceCart",
            Context.MODE_PRIVATE
        )
        val price = prefs.getString("price", null)!!.toInt()
        txt_price.text = (price+1).toString()
    }
}
