package com.example.myapplicationtest2.Adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.*
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card2.view.*
import kotlinx.android.synthetic.main.craft_item.view.cardcraft
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CraftCartMyOrderAdapter(var activity: Activity, var data: MutableList<craftorder>) :
    RecyclerView.Adapter<CraftCartMyOrderAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.cardcraft
        val recyclerview_order = itemView.recyclerview_order
        val btn_checkorder = itemView.btn_checkorder
        val profile_namecarft = itemView.text_order_name
        val profile_desccarft = itemView.text_order_desc
        val txt_order_num = itemView.txt_order_num
        val img_order = itemView.img_order
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.card2, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.profile_namecarft.text = data[position].user_name
        holder.profile_desccarft.text = data[position].user_craft_name
        holder.txt_order_num.text = "الطلبات المقدمة " + data[position].quantity + " طلب"
        Picasso.get().load("${Url.api_img}storage/" + data[position].user_img)
            .error(R.drawable.img).into(holder.img_order)

        var playList = mutableListOf<productcarft>()

//        Getproducts(playList, holder.recyclerview_order, position,data)
//        holder.new_price.text = data[position].product_price

        holder.card.setOnClickListener {

//            val editor: SharedPreferences.Editor =
//                activity.getSharedPreferences("sendCraftProductDetailsss", Context.MODE_PRIVATE).edit()
//            editor.putString("Cid",data[position].order_id )
//            editor.apply()
//
//            val i = Intent(this.activity, CraftProfile::class.java)
//            activity.startActivity(i)
//        }
            holder.btn_checkorder.setOnClickListener {

                holder.recyclerview_order.visibility = View.VISIBLE
//
//            val editor: SharedPreferences.Editor =
//                activity.getSharedPreferences("sendCraftProductDetailsss", Context.MODE_PRIVATE).edit()
//            editor.putString("Cid",data[position].order_id )
//            editor.apply()

//            val i = Intent(this.activity, CraftProfile::class.java)
//            activity.startActivity(i)
            }

        }

        fun Getproducts(
            playList: MutableList<productcarft>,
            recyclerView: RecyclerView,
            position: Int,
            data1: MutableList<userproduct>
        ) {
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
                            val dataproduct = case.getJSONArray("product")
                            Log.e("products", "$data")
                            for (i in 0 until dataproduct.length()) {
                                val caseproduct = dataproduct.getJSONObject(i)
                                if (caseproduct.getString("user_id") == data1[position].user_id) {
                                    playList.add(
                                        productcarft(
                                            caseproduct.getString("id"),
                                            caseproduct.getString("user_id"),
                                            caseproduct.getString("image"),
                                            caseproduct.getString("name"),
                                            caseproduct.getString("description"),

                                            )
                                    )
                                }

                            }


                        }
                        Log.e("AllProducts caseproduct", "$playList")

                        val ProducCraftAdapter = ProducCraftAdapter(this.activity, playList)
                        recyclerView.adapter = ProducCraftAdapter
                        recyclerView.layoutManager = LinearLayoutManager(
                            this.activity,
                            LinearLayoutManager.HORIZONTAL, false
                        )

                        recyclerView.setHasFixedSize(true)
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

}
