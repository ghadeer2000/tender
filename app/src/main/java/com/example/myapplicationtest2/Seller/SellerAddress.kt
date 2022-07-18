package com.example.myapplicationtest2.Seller

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import kotlinx.android.synthetic.main.activity_address_seller.*
import kotlinx.android.synthetic.main.activity_address_seller.view.*
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class SellerAddress : AppCompatActivity() {
    var id: String = ""
    var city_name: String = ""
    var region_text: String = ""
    var street_text: String = ""
    var detailed_address_txt: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_seller)

        btn_save.setOnClickListener {
            UpdateAddress()

        }
        getAddresss()
    }
    private fun UpdateAddress() {
        val url = Url.api + "buyer/address/update"
        val token = com.example.myapplicationtest2.MainActivity2.tokinbuyer
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)

                Log.e("updateAddress", "${data}")
                Toast.makeText(this, "تم تعديل البيانات بنجاح ", Toast.LENGTH_SHORT).show()


            },
            Response.ErrorListener { error ->
                Log.e("error Listener updateAddress", "${error.message}")
            }) {


            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params["id"] = id
                params["city_name"] = city.txt_city.text.toString()
                params["area_name"] = region.txt_place.text.toString()
                params["street_name"] = street.txt_street.text.toString()
                params["detailed_address"] = detailed_address.txt_detailed_address.text.toString()
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
        val url = Url.api + "buyer/myAddress"
        val token = com.example.myapplicationtest2.MainActivity2.tokinbuyer

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
                        city.txt_city.setText(case.getString("city_name"))
                        region.txt_place.setText(case.getString("area_name"))
                        street.txt_street.setText(case.getString("street_name"))
                        detailed_address.txt_detailed_address.setText(case.getString("detailed_address"))

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
}
