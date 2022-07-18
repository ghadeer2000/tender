package com.example.myapplicationtest2.Seller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationprojecttender.model.newproduct
import com.example.myapplicationtest2.Adapter.AnthorProductAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_details.*
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class ProductDetails : AppCompatActivity() {
    lateinit var productId: String
    lateinit var userId: String
    lateinit var cat: String
    lateinit var status: String
    var count: Int = 1
    lateinit var playListAnthorProduct: MutableList<newproduct>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        playListAnthorProduct = mutableListOf<newproduct>()
        val prefs: SharedPreferences = this.getSharedPreferences(
            "sendProductDetails",
            Context.MODE_PRIVATE
        )
        val from = prefs.getString("from", null)


        if (from == "home") {
            productId = prefs.getString("Nid", null)!!
            userId = prefs.getString("NuserId", null)!!
            val name = prefs.getString("Nname", null)
            val img = prefs.getString("NImg", null)
            val desc = prefs.getString("NDesc", null)
            val price = prefs.getString("NPrice", null)
            cat = prefs.getString("NCat", null)!!


            Picasso.get().load("${Url.api_img}storage/" + img)
                .error(R.drawable.img).into(image_product)

            txt_name.text = name
            txt_price.text = price
            txt_desc.text = desc
            txt_name.text = name
        }
        getSellers()

        btn_follow.setOnClickListener {
            follows(userId)
            if (status == "unfollow") {
                btn_follow.text = "إلغاء المتابعة"
            } else {
                btn_follow.text = " متابعة"

            }
        }
        save.setOnClickListener {
            addtoSaved(productId)
        }
        getAnthorproduct()

        plus_Count.setOnClickListener {
            count++
            txt_count.text = count.toString()
        }
        mins_Count.setOnClickListener {
            count--
            txt_count.text = count.toString()
        }
        btn_add.setOnClickListener {
            addtoCart(productId, txt_count.text.toString())

        }
        Log.e("txt_count", "${productId} ${txt_count.text.toString()}")

    }
    fun getSellers() {
        val url = Url.api + "buyer/getSellers"
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

                    val data = response.getJSONArray("sellers")
                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        if (userId == case.getString("id")) {
                            var img = case.getString("image")
                            Picasso.get().load("${Url.api_img}storage/" + img)
                                .error(R.drawable.img).into(profile_image)
                            txt_name_craft.text = case.getString("name")
                            txt_craft.text = case.getString("craft_name")
                        }


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

    private fun follows(seller_id: String) {
        val url = Url.api + "buyer/follow"
        val token = com.example.myapplicationtest2.MainActivity2.tokinbuyer
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)
                var state = data.getString("message")
                if (state == "unfollow") {
                    status = "unfollow"
                } else {
                    status = "follow"

                }
                Log.e("error Listener", "${data}")


            },
            Response.ErrorListener { error ->
                Log.e("error Listener", "${error.message}")
            }) {


            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params["seller_id"] = seller_id
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

    private fun addtoCart(id: String, quantity: String) {
        val url = Url.api + "buyer/cart/create"
        val token = com.example.myapplicationtest2.MainActivity2.tokinbuyer
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)
                Toast.makeText(this, "تمت الاضافة", Toast.LENGTH_SHORT).show()

                Log.e("addtoCart", "${data}")


            },
            Response.ErrorListener { error ->
                Log.e("error Listener addtoCart", "${error.message}")
            }) {


            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params["product_id"] = id
                params["quantity"] = quantity
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

    fun getAnthorproduct() {
        val url = Url.api + "seller/categories"
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

                    val data = response.getJSONArray("categories")

                    Log.e("SavedProducts", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        val dataproduct = case.getJSONArray("products")
                        if (case.getString("name") == cat)
                            for (i in 0 until dataproduct.length()) {
                                val caseproduct = dataproduct.getJSONObject(i)
                                playListAnthorProduct.add(
                                    newproduct(
                                        caseproduct.getString("id"),
                                        caseproduct.getString("user_id"),
                                        case.getString("name"),
                                        caseproduct.getString("image"),
                                        caseproduct.getString("name"),
                                        caseproduct.getString("description"),
                                        caseproduct.getString("price")
                                    )
                                )
                            }
                    }
                    val AnthorProductAdapter =
                        AnthorProductAdapter(this as Activity, playListAnthorProduct)
                    recycler_product.adapter = AnthorProductAdapter

                    recycler_product.layoutManager = LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    recycler_product.setHasFixedSize(true)
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


    private fun addtoSaved(id: String) {
        val url = Url.api + "buyer/savedProducts/create"
        val token = com.example.myapplicationtest2.MainActivity2.tokinbuyer
        val queue = Volley.newRequestQueue(this)
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                Log.e("add toSaved", response)
                val data = JSONObject(response)
                var message = data.getString("message")
                Toast.makeText(this,"تم الحفظ ", Toast.LENGTH_SHORT).show()
//                if (message == "تم حفظ المنتج") {
//
//                } else {
//
//                }

            },
            Response.ErrorListener { error ->
                Log.e("add toSaved", "${error.message}")

            }) {

            override fun getParams(): MutableMap<String, String> {

                var params = HashMap<String, String>()
                params["product_id"] = id

                return params
            }


            override fun getHeaders(): MutableMap<String, String> {
                val headers =
                    HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                headers["Accept"] = "application/json"

                return headers
            }


        }
        queue.add(request)

    }
}
