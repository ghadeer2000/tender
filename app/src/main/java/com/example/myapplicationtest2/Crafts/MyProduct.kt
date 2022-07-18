package com.example.myapplicationtest2.Crafts

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.myproduct
import com.example.myapplicationprojecttender.model.product
import com.example.myapplicationtest2.Adapter.MyProductAdapter
import com.example.myapplicationtest2.Adapter.SavedAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_address.view.*
import kotlinx.android.synthetic.main.activity_myproducts.*
import kotlinx.android.synthetic.main.activity_saved_product.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class MyProduct : AppCompatActivity() {
    lateinit var playList: MutableList<myproduct>
    var tokinCraft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myproducts)
        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        playList = mutableListOf<myproduct>()
        Getproducts()
    }

    fun Getproducts() {
        val url = Url.api + "seller/myProducts"
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

                    val data = response.getJSONArray("product")
                    Log.e("AllProducts", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        val datacat = case.getJSONObject("category")

                        playList.add(
                            myproduct(
                                case.getString("id"),
                                datacat.getString("id"),
                                case.getString("image"),
                                case.getString("name"),
                                case.getString("description"),
                                case.getString("price")
                            )
                        )


                    }
                    Log.e("AllProducts", "$playList")

                    val myProductAdapter = MyProductAdapter(this, playList)
                    recycler_myproduct.adapter = myProductAdapter

                    recycler_myproduct.layoutManager = LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL, false
                    )

                    recycler_myproduct.setHasFixedSize(true)
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
