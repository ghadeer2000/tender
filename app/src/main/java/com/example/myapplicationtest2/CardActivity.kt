package com.example.myapplicationtest2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationprojecttender.model.userproduct
import com.example.myapplicationtest2.Adapter.CategoryAdapter
import kotlinx.android.synthetic.main.activity_card.*
import kotlinx.android.synthetic.main.fragment_craftsmen_crafts.view.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CardActivity : AppCompatActivity() {
    lateinit var playList: MutableList<category>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)
        playList = mutableListOf<category>()
//        Getproducts()
    }

    fun Getproducts() {
        val url = Url.api + "seller/mayLikeSellers"
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

                    val data = response.getJSONArray("products")
                    Log.e("AllProducts", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        val data = response.getJSONArray("products")
                        val dataproduct = case.getJSONArray("product")

                        Log.e("products", "$data")


                        for (i in 0 until dataproduct.length()) {
                            val caseproduct = dataproduct.getJSONObject(i)
                            playList.add(
                                category(
                                    caseproduct.getString("image")
                                )
                            )
                        }


                    }
                    Log.e("AllProducts", "$playList")

                    val CategoryAdapter = CategoryAdapter(this as Activity, playList)
                    recyclerviewcarftproduct.adapter = CategoryAdapter
                    recyclerviewcarftproduct.layoutManager = LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    recyclerviewcarftproduct.setHasFixedSize(true)
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