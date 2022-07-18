package com.example.myapplicationtest2.Seller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationtest2.HomeFragment
import com.example.myapplicationtest2.MainActivity2
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import kotlinx.android.synthetic.main.activity_done.*
import org.json.JSONObject

class DoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_done)

        btn_next.setOnClickListener {
            addtoBuying()
            val i = Intent(this, MainActivity2::class.java)
            startActivity(i)
        }
    }

    private fun addtoBuying() {
        val url = Url.api + "buyer/purchaseCart/buying"
        val token = MainActivity2.tokinbuyer
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
