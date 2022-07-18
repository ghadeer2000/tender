package com.example.myapplicationtest2.Seller

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationtest2.Adapter.CategoryAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_address.view.*
import kotlinx.android.synthetic.main.activity_my_request.*
import kotlinx.android.synthetic.main.fragment_craftsmen_crafts.view.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class MyRequest : AppCompatActivity() {
    lateinit var playList: MutableList<category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_request)
        playList = mutableListOf<category>()

        getOrders()
    }

    fun getOrders() {
        val url = Url.api + "seller/order/getOrders"
        val token = Url.tokinSeller

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
