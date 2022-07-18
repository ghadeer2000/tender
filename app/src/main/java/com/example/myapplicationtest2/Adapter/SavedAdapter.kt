package com.example.myapplicationtest2.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationprojecttender.model.product
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import kotlinx.android.synthetic.main.product_item.view.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile_crafts.view.*
import org.json.JSONObject

class SavedAdapter(var activity: Activity, var data: MutableList<product>) :
    RecyclerView.Adapter<SavedAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val saved_img = itemView.imvCircular
        val saved_product_name = itemView.nameProduct
        val saved_price = itemView.price
        val delete= itemView.delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.product_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.saved_product_name.text = data[position].product_name
        Picasso.get().load("${Url.api_img}storage/" + data[position].product_img)
            .error(R.drawable.img).into(holder.saved_img)
//        Picasso.get().load(R.drawable.img)
//         .error(R.drawable.img).into(holder.saved_img)
      //  holder.saved_img.setImageResource(data[position].product_img)
        holder.saved_price.text = data[position].product_price
        var tokinCraft: String = ""
        var sharedPreferences =
            activity.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        holder.delete.setOnClickListener {
            deleteProductSaved(data[position].id,position,holder,tokinCraft)


        }



    }
    private fun deleteProductSaved(id:String,position: Int,holder: MyViewHolder, tokinCraft: String){
        val url = Url.api + "seller/SavedProducts/delete"
        val token = MainActivity2.tokinCraft
        val queue= Volley.newRequestQueue(activity)
        val request = object : StringRequest(
            Request.Method.POST,url,
            Response.Listener { response ->

                Log.e("deleteAddress","$response")
                data.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(holder.adapterPosition,data.size)

            },
            Response.ErrorListener { error ->
                Log.e("deleteAddress","${error.message}")

            }){
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params["id"] = id
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
