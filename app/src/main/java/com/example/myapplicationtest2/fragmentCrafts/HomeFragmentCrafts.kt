package com.example.myapplicationtest2.fragmentCrafts

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationprojecttender.model.newproduct
import com.example.myapplicationprojecttender.model.product
import com.example.myapplicationtest2.Adapter.CategoryAdapter
import com.example.myapplicationtest2.Adapter.NewProductAdapter
import com.example.myapplicationtest2.Adapter.SavedAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import kotlinx.android.synthetic.main.fragment_home_crafts.view.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class HomeFragmentCrafts : Fragment() {
    lateinit var playList: MutableList<newproduct>
    lateinit var playListOffer: MutableList<newproduct>
    lateinit var playListCat: MutableList<category>
    var tokinCraft: String = ""


    lateinit var root: View
    var result = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home_crafts, container, false)
        var sharedPreferences = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
//        Log.e("nop", MainActivity2.tokinCraft)
        Log.e("nop", tokinCraft)
        playList = mutableListOf<newproduct>()
        playListOffer = mutableListOf<newproduct>()
        playListCat = mutableListOf<category>()

        GetNewProduct()
        GetOfferProduct()
        getCategories()





        return root
    }

    fun getCategories() {
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

                    Log.e("categories", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        playListCat.add(
                            category(
                                case.getString("image")
                            )
                        )

                    }
                    val CategoryAdapter = CategoryAdapter(this.context as Activity, playListCat)
                    root.recyclerViewCategory.adapter = CategoryAdapter
                    root.recyclerViewCategory.layoutManager = LinearLayoutManager(
                        this.requireContext(),
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    root.recyclerViewCategory.setHasFixedSize(true)

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

    fun GetNewProduct() {
        val url = Url.api + "seller/products/lastProducts"
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


                    Log.e("SavedProducts", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        playList.add(
                            newproduct(
                                case.getString("id"),
                                case.getString("user_id"),
                                case.getJSONObject("category").getString("name"),
                                case.getString("image"),
                                case.getString("name"),
                                case.getString("description"),
                                case.getString("price")
                            )
                        )


                    }
                    val newProductAdapter = NewProductAdapter(this.requireActivity(), playList)
                    root.recyclerViewNew.adapter = newProductAdapter

                    root.recyclerViewNew.layoutManager = LinearLayoutManager(
                        this.requireContext(),
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    root.recyclerViewNew.setHasFixedSize(true)
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

    fun GetOfferProduct() {
        val url = Url.api + "seller/productsOffers"
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
                    Log.e("SavedProducts", "$token")

                    val data = response.getJSONArray("products")


                    Log.e("SavedProducts", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        playListOffer.add(
                            newproduct(
                                case.getString("id"),
                                case.getString("user_id"),
                                case.getJSONObject("category").getString("name"),
                                case.getString("image"),
                                case.getString("name"),
                                case.getString("description"),
                                case.getString("price")
                            )
                        )


                    }
                    Log.e("playListOffer", "$playListOffer")

                    val newProductAdapter =
                        NewProductAdapter(this.context as Activity, playListOffer)
                    root.recyclerViewOffer.adapter = newProductAdapter
                    root.recyclerViewOffer.layoutManager = LinearLayoutManager(
                        this.requireContext(),
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    root.recyclerViewOffer.setHasFixedSize(true)
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
