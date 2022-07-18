package com.example.myapplicationtest2.Login

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationtest2.Crafts.Address
import com.example.myapplicationtest2.Crafts.CreateAddresActivity
import kotlinx.android.synthetic.main.activity_sign_up_craft.*
import kotlinx.android.synthetic.main.activity_sign_up_craft.btnLoginBuyer
import kotlinx.android.synthetic.main.activity_sign_up_craft.btn_buyer
import kotlinx.android.synthetic.main.activity_sign_up_craft.btn_craft
import kotlinx.android.synthetic.main.activity_sign_up_craft.btn_login_craft
import kotlinx.android.synthetic.main.activity_sign_up_craft.buyer_layout
import kotlinx.android.synthetic.main.activity_sign_up_craft.craft_layout
import kotlinx.android.synthetic.main.activity_sign_up_craft.email_buyer
import kotlinx.android.synthetic.main.activity_sign_up_craft.name
import kotlinx.android.synthetic.main.activity_sign_up_craft.name_buyer
import kotlinx.android.synthetic.main.activity_sign_up_craft.password_buyer
import kotlinx.android.synthetic.main.activity_sign_up_craft.txt_email_buyer
import kotlinx.android.synthetic.main.activity_sign_up_craft.txt_name
import kotlinx.android.synthetic.main.activity_sign_up_craft.txt_name_buyer
import kotlinx.android.synthetic.main.activity_sign_up_craft.txt_password_buyer
import org.json.JSONException
import org.json.JSONObject
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import java.net.URL

class SignUpCraft : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_craft)

        dialog = ProgressDialog(this)
        dialog!!.setCancelable(false)

        btn_buyer.setOnClickListener {
            changeStyleBuyer()
        }

        btn_craft.setOnClickListener {
            changeStyleCraft()

        }

        btnLoginBuyer.setOnClickListener {
            if(validateBuyer()){
//                registerBuyer()
                var i =Intent(this, CreateAddresActivity::class.java)
                i.putExtra("user","buyer")
                startActivity(i)
                finish()
            }

        }
        btn_login_craft.setOnClickListener {
            if(validateCraft()){
//                registerCraft()
                var i =Intent(this, CreateAddresActivity::class.java)
                i.putExtra("user","craft")
                startActivity(i)
                finish()
            }

        }


        txt_name_buyer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_name_buyer.text.toString().isNotEmpty()) {
                    name_buyer.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_mobile_number_buyer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_mobile_number_buyer.text.toString().isNotEmpty()) {
                    mobile_number_buyer.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_email_buyer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_email_buyer.text.toString().isNotEmpty()) {
                    email_buyer.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_password_buyer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_password_buyer.text.toString().isNotEmpty()) {
                    password_buyer.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_name.text.toString().isNotEmpty()) {
                    name.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_craft_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_craft_name.text.toString().isNotEmpty()) {
                    craft_name.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_password.text.toString().isNotEmpty()) {
                    password.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_email.text.toString().isNotEmpty()) {
                    email.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        txt_mobile_number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (txt_mobile_number.text.toString().isNotEmpty()) {
                    mobile_number.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }
    private fun validateBuyer(): Boolean {
        if (txt_email_buyer.text.toString().isEmpty()) {
            email_buyer.isErrorEnabled = true
            email_buyer.error = "الايميل مطلوب!"
            return false
        }
        if (txt_name_buyer.text.toString().isEmpty()) {
            name_buyer.isErrorEnabled = true
            name_buyer.error = "الاسم مطلوب!"
            return false
        }
        if (txt_password_buyer.text.toString().length < 8) {
            password_buyer.isErrorEnabled = true
            password_buyer.error = "يجب الا تقل عن 8 احرف"
            return false
        }
        if (txt_mobile_number_buyer.text.toString().length < 10) {
            mobile_number_buyer.isErrorEnabled = true
            mobile_number_buyer.error = "ادخل رقم جوال صالح"
            return false
        }
        return true
    }

    private fun validateCraft(): Boolean {
        if (txt_name.text.toString().isEmpty()) {
            name.isErrorEnabled = true
            name.error = "الاسم مطلوب!"
            return false
        }
        if (txt_email.text.toString().isEmpty()) {
            email.isErrorEnabled = true
            email.error = "الايميل مطلوب!"
            return false
        }
        if (txt_craft_name.text.toString().isEmpty()) {
            craft_name.isErrorEnabled = true
            craft_name.error = "مسمى الحرفة اليدوية مطلوب!"
            return false
        }

        if (txt_password.text.toString().length < 8) {
            password.isErrorEnabled = true
            password.error = "يجب الا تقل عن 8 احرف"
            return false
        }
        if (txt_mobile_number.text.toString().length < 10) {
            mobile_number.isErrorEnabled = true
            mobile_number.error = "ادخل رقم جوال صالح!"
            return false
        }

        return true
    }

    private fun registerBuyer() {
        var api = "${Url.api}buyer/register"
        dialog!!.setMessage("Registering")
        dialog!!.show()

        val stringRequest = object :StringRequest(Method.POST,api, Response.Listener { response ->
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
            Toast.makeText(this,response,Toast.LENGTH_LONG).show()

        },Response.ErrorListener { error ->
            Toast.makeText(this,"$error",Toast.LENGTH_LONG).show()
            dialog!!.dismiss()

        }){
            override fun getParams(): MutableMap<String, String>? {
                val params:HashMap<String, String> = HashMap()
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

    private fun changeStyleBuyer(){
        craft_layout.visibility = View.GONE
        buyer_layout.visibility = View.VISIBLE
        btn_buyer.setBackgroundResource(R.drawable.button_green)
        btn_buyer.setTextColor(Color.WHITE)
        btn_craft.setBackgroundResource(R.drawable.buttonseller)
        btn_craft.setTextColor(Color.BLACK)
    }
    private fun changeStyleCraft(){
        buyer_layout.visibility = View.GONE
        craft_layout.visibility = View.VISIBLE
        btn_craft.setBackgroundResource(R.drawable.button_green)
        btn_craft.setTextColor(Color.WHITE)
        btn_buyer.setBackgroundResource(R.drawable.buttonseller)
        btn_buyer.setTextColor(Color.BLACK)
    }

    private fun registerCraft(){
        var api = "${Url.api}seller/register"
        dialog!!.setMessage("Registering")
        dialog!!.show()

        val stringRequest = object :StringRequest(Method.POST,
            api, Response.Listener { response ->
            dialog!!.dismiss()
            val jsonObject = JSONObject(response)
            if (jsonObject.getBoolean("success")) {
                val sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("token", jsonObject.getString("token"))
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                startActivity(Intent(this, com.example.myapplicationtest2.MainActivity2::class.java))
                finish()
            }
            Toast.makeText(this,response,Toast.LENGTH_LONG).show()

        },Response.ErrorListener { error ->
            Toast.makeText(this,"$error",Toast.LENGTH_LONG).show()
            dialog!!.dismiss()

        }){
            override fun getParams(): MutableMap<String, String>? {
                val params:HashMap<String, String> = HashMap()
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