package com.example.myapplicationtest2.Crafts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detials.*
import kotlinx.android.synthetic.main.activity_detials.txt_name
import kotlinx.android.synthetic.main.activity_detials.txt_price
import kotlinx.android.synthetic.main.activity_product_details.*

class EditDetialsActivity : AppCompatActivity() {
    lateinit var productId: String
    lateinit var catId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detials)

        btn_edit_myproduct.setOnClickListener {
            val i = Intent(this, Details::class.java)
            startActivity(i)
        }


        val prefs: SharedPreferences = this.getSharedPreferences(
            "sendmyProductDetails",
            Context.MODE_PRIVATE
        )
        val from = prefs.getString("from", null)


        if (from == "Myproduct") {
            productId = prefs.getString("MyproductNid", null)!!
            catId = prefs.getString("MyproductNcatId", null)!!
            val name = prefs.getString("MyproductNname", null)
            val img = prefs.getString("MyproductNImg", null)
            val desc = prefs.getString("MyproductNDesc", null)
            val price = prefs.getString("MyproductNPrice", null)


            Picasso.get().load("${Url.api_img}storage/" + img)
                .error(R.drawable.img).into(edit_img)

            txt_name.text = name
            txt_price.text = price
            txt_dec.text = desc
        }
    }
}
