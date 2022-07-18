package com.example.myapplicationtest2.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationprojecttender.model.newproduct
import com.example.myapplicationtest2.Crafts.ProductDetails
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_layout.view.*

class AnthorProductAdapter(var activity: Activity, var data: MutableList<newproduct>) :
    RecyclerView.Adapter<AnthorProductAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.card_cat
        val cat_img = itemView.cat_img

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.category_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Picasso.get().load("${Url.api_img}storage/" + data[position].product_image)
            .error(R.drawable.img).into(holder.cat_img)


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

    }
}
