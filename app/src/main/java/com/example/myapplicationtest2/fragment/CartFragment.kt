package com.example.myapplicationtest2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.cart
import com.example.myapplicationprojecttender.model.craftorder
import com.example.myapplicationprojecttender.model.product2
import com.example.myapplicationtest2.Adapter.CartSellAdapter
import com.example.myapplicationtest2.Crafts.CraftFirstStepPayActivity
import com.example.myapplicationtest2.R
import kotlinx.android.synthetic.main.fragment_cart.view.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CartFragment : Fragment() {

    lateinit var playList: MutableList<String>
    lateinit var playListcart: MutableList<cart>
    lateinit var playListproduct: MutableList<product2>
    lateinit var playListorder: MutableList<craftorder>
    lateinit var root: View
    var price = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_cart, container, false)
        playList = mutableListOf<String>()
        playListcart = mutableListOf<cart>()
        playListproduct = mutableListOf<product2>()
        playListorder = mutableListOf<craftorder>()

        GetSellCart()



        return root
    }
    fun GetSellCart() {
        val url = Url.api + "buyer/cart"
        val token = MainActivity2.tokinbuyer

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
                                "1",
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

}
