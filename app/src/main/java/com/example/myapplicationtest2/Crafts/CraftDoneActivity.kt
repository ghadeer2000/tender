package com.example.myapplicationtest2.Crafts

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import kotlinx.android.synthetic.main.activity_craft_done.*
import org.json.JSONObject

class CraftDoneActivity : AppCompatActivity() {
    var tokinCraft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_craft_done)
        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        btn_next.setOnClickListener {
            addtoBuying()
            val i = Intent(
                this,
                com.example.myapplicationtest2.fragmentCrafts.MainActivity2::class.java
            )
            startActivity(i)
        }

    }

    private fun addtoBuying() {
        val url = Url.api + "seller/purchaseCart/buying"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)
                // Toast.makeText(this, "تمت الطلب بنجاح", Toast.LENGTH_SHORT).show()

                Log.e("addtoBuying", "${data}")


            },
            Response.ErrorListener { error ->
                Log.e("error Listener addtoBuying", "${error.message}")
            }) {


            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()

                return params
            }


            override fun getHeaders(): MutableMap<String, String> {
                val headers =
                    HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                return headers
            }


        }

        queue.add(request)

    }

}