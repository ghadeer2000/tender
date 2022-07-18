package com.example.myapplicationtest2.Crafts

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationtest2.MainActivity2
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import kotlinx.android.synthetic.main.activity_create_addres.*
import kotlinx.android.synthetic.main.activity_sign_up_craft.*
import org.json.JSONObject

class CreateAddresActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_addres)

        dialog = ProgressDialog(this)
        dialog!!.setCancelable(false)
        btn_save.setOnClickListener {
            var user = intent.getStringExtra("user")
            if (user == "buyer") {
                registerBuyer()
            } else {
                registerCraft()
            }
            startActivity(Intent(this, MainActivity2::class.java))
            finish()


        }
    }

    private fun registerBuyer() {
        var api = "${Url.api}buyer/register"
        dialog!!.setMessage("Registering")
        dialog!!.show()

        val stringRequest = object : StringRequest(Method.POST, api, Response.Listener { response ->
            dialog!!.dismiss()
            val jsonObject = JSONObject(response)
            if (jsonObject.getBoolean("success")) {
                val sharedPreferences = getSharedPreferences("buyer", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("token", jsonObject.getString("token"))
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                startActivity(Intent(this, Address::class.java))
                finish()
            }
            Toast.makeText(this, response, Toast.LENGTH_LONG).show()

        }, Response.ErrorListener { error ->
            Toast.makeText(this, "$error", Toast.LENGTH_LONG).show()
            dialog!!.dismiss()

        }) {
            override fun getParams(): MutableMap<String, String>? {
                val params: HashMap<String, String> = HashMap()
                params["image"] = ""
                params["name"] = txt_name_buyer.text.toString()
                params["email"] = txt_email_buyer.text.toString()
                params["password"] = txt_password_buyer.text.toString()
                params["phone_number"] = txt_mobile_number_buyer.text.toString()
                return params
            }
        }
//        val request: StringRequest =
//            object : StringRequest(Request.Method.POST, URLs.REGISTER_BUYER, { response ->
//                try {
//                    val jsonObject = JSONObject(response)
//                    if (jsonObject.getBoolean("success")) {
//                        val buyer: JSONObject = jsonObject.getJSONObject("buyer")
//                        val sharedPreferences = getSharedPreferences("buyer", MODE_PRIVATE)
//                        val editor = sharedPreferences.edit()
//                        editor.putString("token", jsonObject.getString("token"))
////                        editor.putString("name", buyer.getString("name"))
////                        editor.putInt("id", buyer.getInt("id"))
////                        editor.putString("email", buyer.getString("email"))
////                        editor.putString("phone_number", buyer.getString("phone_number"))
////                        editor.putString("username", buyer.getString("username"))
////                        editor.putString("image", buyer.getString("image"))
//                        editor.putBoolean("isLoggedIn", true)
//                        editor.apply()
//                        startActivity(Intent(this, HomeActivity::class.java))
//                        finish()
//                        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
//                        dialog!!.dismiss()
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//            }, { error ->
//                // error if connection not success
//                error.printStackTrace()
//                dialog!!.dismiss()
//            }) {
//                override fun getParams(): MutableMap<String, String>? {
//                    val params:HashMap<String, String> = HashMap()
//                    params["name"] = txt_name_buyer.text.toString()
//                    params["email"] = txt_email_buyer.text.toString().trim()
//                    params["password"] = txt_password_buyer.text.toString()
//                    params["phone_number"] = txt_mobile_number_buyer.text.toString()
//                    params["image"] = ""
//
//
//                    return params
//                }
//
//
//            }

        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }

    private fun registerCraft() {
        var api = "${Url.api}seller/register"
        dialog!!.setMessage("Registering")
        dialog!!.show()

        val stringRequest = object : StringRequest(Method.POST,
            api, Response.Listener { response ->
                dialog!!.dismiss()
                val jsonObject = JSONObject(response)
                if (jsonObject.getBoolean("success")) {
                    val sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("token", jsonObject.getString("token"))
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    startActivity(
                        Intent(
                            this,
                            com.example.myapplicationtest2.MainActivity2::class.java
                        )
                    )
                    finish()
                }
                Toast.makeText(this, response, Toast.LENGTH_LONG).show()

            }, Response.ErrorListener { error ->
                Toast.makeText(this, "$error", Toast.LENGTH_LONG).show()
                dialog!!.dismiss()

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params: HashMap<String, String> = HashMap()
                params["image"] = ""
                params["name"] = txt_name.text.toString()
                params["email"] = txt_email.text.toString()
                params["password"] = txt_password.text.toString()
                params["craft_name"] = txt_craft_name.text.toString()
                params["phone_number"] = txt_mobile_number.text.toString()
                return params
            }
        }

        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(stringRequest)

    }


}