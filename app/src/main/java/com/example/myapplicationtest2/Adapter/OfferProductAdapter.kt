package com.example.myapplicationtest2.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationprojecttender.model.newproduct
import com.example.myapplicationtest2.Crafts.ProductDetails
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.most_sale.view.*
import org.json.JSONObject

class OfferProductAdapter(var activity: Activity, var data: MutableList<newproduct>) :
    RecyclerView.Adapter<OfferProductAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.card_new
        val btn_saved = itemView.btn_saved
        val new_product_name = itemView.nameProduct
        val new_product_des = itemView.dec
        val new_price = itemView.price
        val new_img = itemView.imgnewproduct
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.most_sale, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.new_product_name.text = data[position].product_name
        holder.new_product_des.text = data[position].product_des
        Picasso.get().load("${Url.api_img}storage/" + data[position].product_image)
            .error(R.drawable.img).into(holder.new_img)

        holder.new_price.text = data[position].product_price
        var tokinCraft: String = ""
        var sharedPreferences =
            activity.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        holder.card.setOnClickListener {
            val editor: SharedPreferences.Editor =
                activity.getSharedPreferences("sendProductDetails", Context.MODE_PRIVATE).edit()
            editor.putString("Nid", data[position].new_id)
            editor.putString("NuserId", data[position].user_id)
            editor.putString("Nname", data[position].product_name)
            editor.putString("NImg", data[position].product_image)
            editor.putString("NPrice", data[position].product_price)
            editor.putString("NDesc", data[position].product_des)
            editor.putString("from", "home")
            editor.apply()

            val i = Intent(this.activity, ProductDetails::class.java)
            activity.startActivity(i)
        }
        holder.btn_saved.setOnClickListener {
            addtoSaved(data[position].new_id!!,holder,tokinCraft)
        }

    }

    private fun addtoSaved(id: String,holder: MyViewHolder, tokinCraft: String) {
        val url = Url.api + "seller/SavedProducts/create"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(activity)
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                Log.e("add toSaved", response)
                val data = JSONObject(response)
                var message = data.getString("message")
                Toast.makeText(activity,"تم الحفظ", Toast.LENGTH_SHORT).show()
//                if (message == "تم حفظ المنتج") {
//                    holder.btn_saved.setBackgroundResource( R.drawable.btn_product2)
//                } else {
//                    holder.btn_saved.setBackgroundResource( R.drawable.btn_product)
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
