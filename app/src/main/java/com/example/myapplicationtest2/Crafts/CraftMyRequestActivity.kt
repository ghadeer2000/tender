package com.example.myapplicationtest2.Crafts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationtest2.Adapter.CategoryAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import kotlinx.android.synthetic.main.activity_craft_my_request.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CraftMyRequestActivity : AppCompatActivity() {
    lateinit var playList: MutableList<category>
    var tokinCraft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_craft_my_request)
        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        playList = mutableListOf<category>()

        getOrders()
    }
    fun getOrders() {
        val url = Url.api + "seller/order/getOrders"
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

                    val data = response.getJSONArray("order")
                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        playList.add(
                            category(
                                case.getString("image")
                            )
                        )


                    }
                    val CategoryAdapter = CategoryAdapter(this, playList)
                    recycler_order_cart_craft.adapter = CategoryAdapter
                    recycler_order_cart_craft.layoutManager = LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    recycler_order_cart_craft.setHasFixedSize(true)

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