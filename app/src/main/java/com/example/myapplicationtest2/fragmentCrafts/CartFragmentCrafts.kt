package com.example.myapplicationtest2.fragmentCrafts

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationprojecttender.model.*
import com.example.myapplicationtest2.Adapter.CartSellAdapter
import com.example.myapplicationtest2.Adapter.CraftCartMyOrderAdapter
import com.example.myapplicationtest2.Adapter.NewProductAdapter
import com.example.myapplicationtest2.Crafts.CraftFirstStepPayActivity
import com.example.myapplicationtest2.Crafts.FollowersActivity
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Seller.FirstStepPuy
import com.example.myapplicationtest2.SwipToDelete
import com.example.myapplicationtest2.Url
import kotlinx.android.synthetic.main.fragment_cart_crafts.view.*
import kotlinx.android.synthetic.main.fragment_home_crafts.view.*
import kotlinx.android.synthetic.main.fragment_profile_crafts.view.*
import kotlinx.android.synthetic.main.reques_item.*
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CartFragmentCrafts : Fragment() {
    lateinit var playList: MutableList<String>
    lateinit var playListcart: MutableList<cart>
    lateinit var playListproduct: MutableList<product2>
    lateinit var playListorder: MutableList<craftorder>
    lateinit var root: View
    var tokinCraft: String = ""
    var price = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        root = inflater.inflate(R.layout.fragment_cart_crafts, container, false)
        playList = mutableListOf<String>()
        playListcart = mutableListOf<cart>()
        playListproduct = mutableListOf<product2>()
        playListorder = mutableListOf<craftorder>()
        var sharedPreferences = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        GetSellCart()

        root.btn_yoursell.setOnClickListener {
            root.linear_top.visibility = View.GONE
            root.linear_top2.visibility = View.VISIBLE
            root.recyclerViewCart.visibility = View.GONE
            root.recyclerViewCartsell.visibility = View.VISIBLE
            GetOrderCart()
        }
        root.btn_sell2.setOnClickListener {
            root.linear_top.visibility = View.VISIBLE
            root.linear_top2.visibility = View.GONE
            root.recyclerViewCart.visibility = View.VISIBLE
            root.recyclerViewCartsell.visibility = View.GONE
        }

        root.btn_pay.setOnClickListener {
            val editor: SharedPreferences.Editor =
                this.requireActivity()!!
                    .getSharedPreferences("sendpriceCart2", Context.MODE_PRIVATE).edit()
            editor.putString("price", price.toString())
            editor.apply()
            val i = Intent(this.context, CraftFirstStepPayActivity::class.java)
            startActivity(i)
        }
        return root
    }

    fun GetSellCart() {
        val url = Url.api + "seller/purchaseCart"
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


                    Log.e("GetSellCart", "$data")

                    var dataproduct = playListproduct

                    for (e in 0 until data.length()) {
                        val case = data.getJSONObject(e)
                        playListcart.add(
                            cart(
                                case.getString("id"),
                                case.getString("product_id"),
                                case.getString("image"),
                                case.getString("name"),
                                case.getString("quantity")

                            )
                        )
                        price += case.getString("price").toInt() * case.getString("quantity")
                            .toInt()

                    }

                    root.full_price.setText("${price.toString()}")

//                    val swipeDelete =
//                        object : SwipToDelete.SwipeToDeleteCallback(this.requireActivity()) {
//                            override fun onSwiped(
//                                viewHolder: RecyclerView.ViewHolder,
//                                direction: Int
//                            ) {
////                            CartSellAdapter.(viewHolder.adapterPosition)
////                                deleteItem()
//
//
//                            }
//                        }

//                    val touchHelper = ItemTouchHelper(swipeDelete)
//                    touchHelper.attachToRecyclerView(root.recyclerViewCart)
                    Log.e("playList playListcart", "$playListproduct")

                    val cartSellAdapter = CartSellAdapter(this.context as Activity, playListcart)
                    root.recyclerViewCart.adapter = cartSellAdapter

                    root.recyclerViewCart.layoutManager = LinearLayoutManager(
                        this.requireContext(),
                        LinearLayoutManager.VERTICAL, false
                    )

                    root.recyclerViewCart.setHasFixedSize(true)

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

    fun GetOrderCart() {
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


                    Log.e("GetOrderCart", "$data")


                    for (e in 0 until data.length()) {
                        val case = data.getJSONObject(e)
                        var data1 = case.getJSONArray("address")
                        for (i in 0 until data1.length()) {
                            var dataadddress = data1.getJSONObject(i)
                            var datauser = dataadddress.getJSONObject("user")
                            playListorder.add(
                                craftorder(
                                    datauser.getString("image"),
                                    data.length().toString(),
                                    case.getString("quantity"),
                                    datauser.getString("id"),
                                    datauser.getString("name"),
                                    datauser.getString("description"),
                                    datauser.getString("phone_number"),
                                    dataadddress.getString("area_name"),
                                    dataadddress.getString("detailed_address")

                                )
                            )
                        }


                    }


                    val CraftCartMyOrderAdapter =
                        CraftCartMyOrderAdapter(this.context as Activity, playListorder)
                    root.recyclerViewCartsell.adapter = CraftCartMyOrderAdapter

                    root.recyclerViewCartsell.layoutManager = LinearLayoutManager(
                        this.requireContext(),
                        LinearLayoutManager.VERTICAL, false
                    )

                    root.recyclerViewCartsell.setHasFixedSize(true)

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
