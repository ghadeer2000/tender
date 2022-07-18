package com.example.myapplicationtest2.SellerLogin

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationtest2.Login.Welcome
import kotlinx.android.synthetic.main.activity_sign_in_seller.*
import org.json.JSONException
import org.json.JSONObject
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import java.net.URL

class SignInSeller : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_seller)
        dialog = ProgressDialog(this)
        dialog!!.setCancelable(false)

        login_btn_buyer.setOnClickListener {
            changeStyleBuyer()
            btn_buyer_login.setOnClickListener {
                if(validateBuyer()){
                    loginBuyer()
                }
            }
        }

        login_btn_craft.setOnClickListener {
            changeStyleCraft()
            btn_craft_login.setOnClickListener {
                if(validateCraft()){
                    loginCraft()
                }
//                dialog!!.show()
//                Handler().postDelayed({
//                    val intent = Intent(this, MainActivity2::class.java)
//                    startActivity(intent)
//                    finish()
//                },3000)
//                dialog!!.dismiss()
//                startActivity(Intent(this, MainActivity2::class.java))
//                finish()
            }

        }

        txt_login_buyer_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_login_buyer_email.text.toString().isNotEmpty()) {
                    login_buyer_email.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_login_buyer_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_login_buyer_password.text.toString().isNotEmpty()) {
                    login_buyer_password.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_login_craft_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_login_craft_email.text.toString().isNotEmpty()) {
                    login_craft_email.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_login_craft_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_login_craft_password.text.toString().isNotEmpty()) {
                    login_craft_password.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun changeStyleBuyer(){
        login_craft_linear.visibility = View.GONE
        login_buyer_linear.visibility = View.VISIBLE
        login_btn_buyer.setBackgroundResource(R.drawable.button_green)
        login_btn_buyer.setTextColor(Color.WHITE)
        login_btn_craft.setBackgroundResource(R.drawable.buttonseller)
        login_btn_craft.setTextColor(Color.BLACK)
    }

    private fun changeStyleCraft(){
        login_buyer_linear.visibility = View.GONE
        login_craft_linear.visibility = View.VISIBLE
        login_btn_craft.setBackgroundResource(R.drawable.button_green)
        login_btn_craft.setTextColor(Color.WHITE)
        login_btn_buyer.setBackgroundResource(R.drawable.buttonseller)
        login_btn_buyer.setTextColor(Color.BLACK)
    }

    private fun validateBuyer(): Boolean {
        if (txt_login_buyer_email.text.toString().isEmpty()) {
            login_buyer_email.isErrorEnabled = true
            login_buyer_email.error = "الايميل مطلوب!"
            return false
        }
        if (txt_login_buyer_password.text.toString().length < 8) {
            login_buyer_password.isErrorEnabled = true
            login_buyer_password.error = "يجب الا تقل عن 8 احرف"
            return false
        }
        return true
    }

    private fun validateCraft(): Boolean {
        if (txt_login_craft_email.text.toString().isEmpty()) {
            login_craft_email.isErrorEnabled = true
            login_craft_email.error = "الايميل مطلوب!"
            return false
        }
        if (txt_login_craft_password.text.toString().length < 8) {
            login_craft_password.isErrorEnabled = true
            login_craft_password.error = "يجب الا تقل عن 8 احرف"
            return false
        }
        return true
    }

    private fun loginBuyer() {
        var api = "${Url.api}buyer/login"
        dialog!!.setMessage("login...")
        dialog!!.show()
        val request: StringRequest =
            object : StringRequest(Request.Method.POST, api, { response ->
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("success")) {
                        val buyer: JSONObject = jsonObject.getJSONObject("buyer")
                        var sharedPreferences = getSharedPreferences("buyer", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("token", jsonObject.getString("token"))
//                        editor.putString("name", buyer.getString("name"))
//                        editor.putInt("id", buyer.getInt("id"))
//                        editor.putString("email", buyer.getString("email"))
//                        editor.putInt("phone_number", buyer.getInt("phone_number"))
//                        editor.putString("image", buyer.getString("image"))
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()
                        startActivity(Intent(this, com.example.myapplicationtest2.MainActivity2::class.java))
                        finish()
                        Toast.makeText(this, "login Success", Toast.LENGTH_SHORT).show()
                        dialog!!.dismiss()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }, { error ->
                // error if connection not success
                error.printStackTrace()
                dialog!!.dismiss()
            }) {
                override fun getParams(): MutableMap<String, String>? {
                    val params:HashMap<String, String> = HashMap()
                    params["email"] = txt_login_buyer_email.text.toString()
                    params["password"] = txt_login_buyer_password.text.toString()

                    return params
                }

            }

        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun loginCraft() {
        var api = "${Url.api}seller/login"
        dialog!!.setMessage("login...")
        dialog!!.show()
        val request: StringRequest =
            object : StringRequest(Request.Method.POST, api, { response ->
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean("success")) {
                        val user: JSONObject = jsonObject.getJSONObject("user")
                        var sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("token", jsonObject.getString("token"))
                        editor.putString("name", user.getString("name"))
                        editor.putInt("id", user.getInt("id"))
                        editor.putString("email", user.getString("email"))
                        editor.putInt("phone_number", user.getInt("phone_number"))
                        editor.putString("image", user.getString("image"))
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()
                        startActivity(Intent(this, MainActivity2::class.java))
                        finish()
                        Toast.makeText(this, "login Success", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                dialog!!.dismiss()
            }, { error ->
                // error if connection not success
                error.printStackTrace()
                dialog!!.dismiss()
            }) {
                override fun getParams(): MutableMap<String, String>? {
                    val params:HashMap<String, String> = HashMap()
                    params["email"] = txt_login_craft_email.text.toString()
                    params["password"] = txt_login_craft_password.text.toString()

                    return params
                }

            }

        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(request)
    }



}
