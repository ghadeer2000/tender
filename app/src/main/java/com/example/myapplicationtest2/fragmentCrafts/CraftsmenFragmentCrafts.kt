package com.example.myapplicationtest2.fragmentCrafts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationprojecttender.model.myproduct
import com.example.myapplicationprojecttender.model.newproduct
import com.example.myapplicationprojecttender.model.userproduct
import com.example.myapplicationtest2.Adapter.AnthorProductAdapter
import com.example.myapplicationtest2.Adapter.CategoryAdapter
import com.example.myapplicationtest2.Adapter.CraftProductAdapter
import com.example.myapplicationtest2.Adapter.MyProductAdapter
import com.example.myapplicationtest2.Crafts.MyProduct
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import kotlinx.android.synthetic.main.activity_myproducts.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.fragment_craftsmen_crafts.*
import kotlinx.android.synthetic.main.fragment_craftsmen_crafts.view.*
import kotlinx.android.synthetic.main.fragment_home_crafts.view.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CraftsmenFragmentCrafts : Fragment() {
    lateinit var root: View
    lateinit var playList: MutableList<category>
    lateinit var playListmylike: MutableList<userproduct>
    lateinit var playListnewseller: MutableList<userproduct>
    var tokinCraft: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_craftsmen_crafts, container, false)
        var sharedPreferences = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        playList = mutableListOf<category>()
        playListmylike = mutableListOf<userproduct>()
        playListnewseller = mutableListOf<userproduct>()

        root.seeMore.setOnClickListener {
            val i = Intent(this.context, MyProduct::class.java)
            startActivity(i)
        }
        Getproducts()
        getUserMyLike()
        getNewSeller()

        return root
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
                            category(
                                case.getString("image")
                            )
                        )


                    }
                    Log.e("AllProducts craft cat", "$playList")

                    val CategoryAdapter = CategoryAdapter(this.requireActivity(), playList)
                    root.recyclercarftmyproduct.adapter = CategoryAdapter
                    root.recyclercarftmyproduct.layoutManager = LinearLayoutManager(
                        this.requireContext(),
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    root.recyclercarftmyproduct.setHasFixedSize(true)
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

    fun getUserMyLike() {
        val url = Url.api + "seller/mayLikeSellers"
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

                    Log.e("products", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        val dataproduct = case.getJSONArray("product")
//                        for (i in 0 until dataproduct.length()) {
//                            val caseproduct = dataproduct.getJSONObject(i)
                        playListmylike.add(
                            userproduct(
                                case.getString("id"),
                                case.getString("name"),
                                case.getString("craft_name"),
                                case.getString("image"),
                                case.getString("number of followers")
                            )
                        )
//                        }
                    }



                    val CraftProductAdapter =
                        CraftProductAdapter(this.requireActivity(), playListmylike)
                    root.recyclercarftmeybelike.adapter = CraftProductAdapter

                    root.recyclercarftmeybelike.layoutManager = LinearLayoutManager(
                        this.requireContext(),
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    root.recyclercarftmeybelike.setHasFixedSize(true)
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

    fun getNewSeller() {
        val url = Url.api + "seller/newSellers"
        val token = MainActivity2.tokinCraft
//        val token = MainActivity2.tokinCraft

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

                    Log.e("products", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        val dataproduct = case.getJSONArray("product")
//                        for (i in 0 until dataproduct.length()) {
//                            val caseproduct = dataproduct.getJSONObject(i)
                        playListmylike.add(
                            userproduct(
                                case.getString("id"),
                                case.getString("name"),
                                case.getString("craft_name"),
                                case.getString("image"),
                                case.getString("number of followers")
                            )
                        )
//                        }
                    }

                    val CraftProductAdapter =
                        CraftProductAdapter(this.requireActivity(), playListmylike)
                    root.recyclercarftnew.adapter = CraftProductAdapter

                    root.recyclercarftnew.layoutManager = LinearLayoutManager(
                        this.requireContext(),
                        LinearLayoutManager.HORIZONTAL, false
                    )

                    root.recyclercarftnew.setHasFixedSize(true)
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
