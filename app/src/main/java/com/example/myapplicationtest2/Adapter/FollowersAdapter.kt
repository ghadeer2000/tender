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
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationprojecttender.model.follower
import com.example.myapplicationprojecttender.model.product
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_layout.view.*
import kotlinx.android.synthetic.main.flowerscard.view.*
import org.json.JSONObject

class FollowersAdapter(var activity: Activity, var data: MutableList<follower>) :
    RecyclerView.Adapter<FollowersAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val follow_img = itemView.follower_img
        val button_remove_follower = itemView.button_remove_follower
        val follower_name = itemView.follower_name
        val follower_desc = itemView.follower_desc

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.flowerscard, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.follower_name.text = data[position].follower_name
        holder.follower_desc.text = data[position].follower_name
        var tokinCraft: String = ""
        var sharedPreferences =
            activity.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        holder.button_remove_follower.setOnClickListener {
            RemoveFollower(data[position].follower_id!!,position,holder,tokinCraft)
        }

        Picasso.get().load("${Url.api_img}storage/" + data[position].follower_img)
            .error(R.drawable.img).into(holder.follow_img)
    }

    private fun RemoveFollower(id:String,position: Int,holder: FollowersAdapter.MyViewHolder, tokinCraft: String) {
        val url = Url.api + "seller/follow"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(activity)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                data.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(holder.adapterPosition,data.size)

                val data = JSONObject(response)

                Log.e("data", "$data")

            },
            Response.ErrorListener { error ->
                Log.e("error Listener", "${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params["seller_id"] = id
                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers =
                    HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer" + token
                return headers
            }


        }



        queue.add(request)

    }

}
