package com.example.myapplicationtest2.Crafts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplicationtest2.R
import kotlinx.android.synthetic.main.activity_craft_delivery.*

class CraftDeliveryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_craft_delivery)
        btn_confirm.setOnClickListener {
            val i = Intent(this, CraftDoneActivity::class.java)
            startActivity(i)
        }
        val prefs: SharedPreferences = this.getSharedPreferences(
            "sendpriceCart2",
            Context.MODE_PRIVATE
        )
        val price = prefs.getString("price", null)!!.toInt()
        txt_price.text = (price+1).toString()

    }
}