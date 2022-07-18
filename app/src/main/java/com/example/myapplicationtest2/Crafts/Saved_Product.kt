package com.example.myapplicationtest2.Crafts

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationtest2.R
import com.example.myapplicationprojecttender.model.product
import com.example.myapplicationtest2.Adapter.SavedAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import kotlinx.android.synthetic.main.activity_saved_product.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class Saved_Product : AppCompatActivity() {

    lateinit var playList: MutableList<product>
    lateinit var idSavedProduct: MutableList<String>
    lateinit var idSavedProductdelete: MutableList<String>
    var tokinCraft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_product)
        playList = mutableListOf<product>()
        idSavedProduct = mutableListOf<String>()
        idSavedProductdelete = mutableListOf<String>()
        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        getSavedProduct()
        Getproducts()


    }



    fun getSavedProduct() {
        val url = Url.api + "seller/SavedProducts"
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

                    Log.e("SavedProducts", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        idSavedProduct.add(case.getString("product_id"))
                        idSavedProductdelete.add(case.getString("id"))
                    }
                    Log.e("idSavedProduct", "$idSavedProduct")
                    Log.e("idSavedProductdelete", "$idSavedProductdelete")

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

    fun Getproducts() {
        val url = Url.api + "seller/products"
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

                    val data = response.getJSONArray("products")

                    Log.e("AllProducts", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        for (i in 0 until idSavedProduct.lastIndex + 1) {
                            if (case.getString("id") == idSavedProduct.get(i)) {
                                    playList.add(
                                        product(
                                            idSavedProductdelete.get(i),
                                            case.getString("name"),
                                            case.getString("price"),
                                            case.getString("image")
                                        )
                                    )


                            }
                        }
                    }
                    Log.e("AllProducts", "$playList")

                    num_saved.text = playList.size.toString()
                    val savedAdapter = SavedAdapter(this, playList)
                    recycler_saved.adapter = savedAdapter

                    recycler_saved.layoutManager = GridLayoutManager(this, 2)

                    recycler_saved.setHasFixedSize(true)
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
