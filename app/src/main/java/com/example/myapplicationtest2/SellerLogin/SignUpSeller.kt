package com.example.myapplicationtest2.SellerLogin

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationtest2.MainActivity2
import com.example.myapplicationtest2.R
import org.json.JSONObject

class SignUpSeller : AppCompatActivity() {
    lateinit var code: String
    lateinit var number: String
    lateinit var deviceToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_seller)


    }
    private fun RegisterSeller() {
        deviceToken = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.e("number", "$number")
        Log.e("number code", "$code")

        val url = "https://jazara.applaab.com/api/checkVerificationCode"
        val queue = Volley.newRequestQueue(
            this
        )
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)
                var stuts = data.getBoolean("status")
                Log.e("status", "${stuts.toString()}")

                if (stuts){
                    startActivity(Intent(this, MainActivity2::class.java))
                    finish()

                }

//                    Log.e("checkVerificati0onCode", "$data")
//                    if (number == code) {
//                        val i = Intent(this, form::class.java)
//                        startActivity(i)
//                    }
                Log.e("checkVerificationCode22", "$code")

            },
            Response.ErrorListener { error ->
                Log.e("checkVerificationCode", "${error.message}")

            }) {

            override fun getParams(): MutableMap<String, String> {

                var params = HashMap<String, String>()
                params["verification_code"] = number
                params["device_token"] = deviceToken

                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers =
                    HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["User-Agent"] = "Mozilla/5.0"
//                headers["Authorization"] = tocken
                return headers
            }
        }



        queue.add(request)

    }
}

