package com.example.myapplicationtest2.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.*
import com.example.myapplicationtest2.Crafts.CraftCraftProfile
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.craft_item.view.*
import kotlinx.android.synthetic.main.craft_item.view.cardcraft
import kotlinx.android.synthetic.main.craft_item.view.followercarft
import kotlinx.android.synthetic.main.craft_item.view.nextcraft
import kotlinx.android.synthetic.main.craft_item.view.profile_desccarft
import kotlinx.android.synthetic.main.craft_item.view.profile_imagecarft
import kotlinx.android.synthetic.main.craft_item.view.profile_namecarft
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CraftProductAdapter(var activity: Activity, var data: MutableList<userproduct>) :
    RecyclerView.Adapter<CraftProductAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.cardcraft
        val recycler_cart_craft = itemView.recycler_cart_craft
        val nextcraft = itemView.nextcraft
        val profile_namecarft = itemView.profile_namecarft
        val profile_desccarft = itemView.profile_desccarft
        val followercarft = itemView.followercarft
        val profile_imagecarft = itemView.profile_imagecarft
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.craft_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.profile_namecarft.text = data[position].user_name
        holder.profile_desccarft.text = data[position].user_craft
        holder.followercarft.text = "متابع " + data[position].number_of_followers
        Picasso.get().load("${Url.api_img}storage/" + data[position].user_img)
            .error(R.drawable.img).into(holder.profile_imagecarft)
        var tokinCraft: String = ""
        var sharedPreferences =
            activity.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        var playList = mutableListOf<productcarft>()

        Getproducts(playList, holder.recycler_cart_craft, position,data,tokinCraft)
//        holder.new_price.text = data[position].product_price

        holder.card.setOnClickListener {


            val editor: SharedPreferences.Editor =
                activity.getSharedPreferences("sendCraftProductDetails", Context.MODE_PRIVATE).edit()
            editor.putString("Cid",data[position].user_id )
            editor.apply()

            val i = Intent(this.activity, CraftCraftProfile::class.java)
            activity.startActivity(i)
        }
        holder.nextcraft.setOnClickListener {
            val editor: SharedPreferences.Editor =
                activity.getSharedPreferences("sendCraftProductDetails", Context.MODE_PRIVATE).edit()
            editor.putString("Cid",data[position].user_id )
            editor.apply()

            val i = Intent(this.activity, CraftCraftProfile::class.java)
            activity.startActivity(i)
        }

    }

    fun Getproducts(
        playList: MutableList<productcarft>,
        recyclerView: RecyclerView,
        position: Int,
        data1: MutableList<userproduct>
        , tokinCraft: String
    ) {
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
