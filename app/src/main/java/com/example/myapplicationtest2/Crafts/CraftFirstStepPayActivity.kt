package com.example.myapplicationtest2.Crafts

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import kotlinx.android.synthetic.main.activity_craft_first_step_pay.*
import kotlinx.android.synthetic.main.activity_craft_first_step_pay.view.*
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CraftFirstStepPayActivity : AppCompatActivity() {
    var id: String = ""
    lateinit var id_user: String
    lateinit var phone: String
    lateinit var city: String
    lateinit var street: String
    var tokinCraft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_craft_first_step_pay)
        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        btn_next.setOnClickListener {
            val i = Intent(this, CraftDeliveryActivity::class.java)
            startActivity(i)
            UpdateAddress()
        }
        GetProfileInformation()
        getAddresss()
    }

    private fun UpdateAddress() {
        val url = Url.api + "seller/address/update"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)

                Log.e("updateAddress", "${data}")


            },
            Response.ErrorListener { error ->
                Log.e("error Listener updateAddress", "${error.message}")
            }) {


            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params["city_name"] = city
                params["area_name"] = editTextPhonefirstStep.txt_placefirstStep.text.toString()
                params["street_name"] = street
                params["detailed_address"] =
                    editTextPhonefirstStep2.txt_addressfirestStep.text.toString()
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

    fun getAddresss() {
        val url = Url.api + "seller/myAddress"
        val token = MainActivity2.tokinCraft

        val cache = MySingleton.getInstance()!!.getRequestQueue()!!.cache
        val entry = cache.get(url)
        if (entry != null) {
            try {
                val da = String(entry!!.data, Charset.forName("UTF-8"))


            } catch (e: UnsupportedEncodingException) {
                Log.e("no", e.message!!)
            }
        } else {

            val jsonObject = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->

                    val data = response.getJSONArray("address")
                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        editTextPhonefirstStep.txt_placefirstStep.setText(case.getString("area_name"))
                        editTextPhonefirstStep2.txt_addressfirestStep.setText(case.getString("detailed_address"))
                        city = case.getString("city_name")
                        street = case.getString("street_name")
                        id = case.getString("id")


                    }

                },
                Response.ErrorListener { error ->
                    Log.e("error Listener", "${error.message}")
                }

            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers =
                        HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token"
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
            MySingleton.getInstance()!!.addRequestQueue(jsonObject)
        }


    }

    private fun GetProfileInformation() {
        val url = Url.api + "seller/userProfile"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)
                id_user = data.getString("id")
                phone = data.getString("phone_number")
                var name = data.getString("name")
                Log.e("data", "$data")
                editTextPhonefirstStep4.txt_phonefirstStep.setText(phone)
                editTextPhonefirstStep3.txt_namefirstStep!!.setText(name)


            },
            Response.ErrorListener { error ->
                Log.e("error Listener", "${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()

                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers =
                    HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer" + token
                return headers
            }


        }



        queue.add(request)

    }


}