package com.example.myapplicationtest2.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationprojecttender.model.myproduct
import com.example.myapplicationtest2.Crafts.EditDetialsActivity
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card3.view.*

class MyProductAdapter(var activity: Activity, var data: MutableList<myproduct>) :
    RecyclerView.Adapter<MyProductAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.card_myproduct
        val next = itemView.next
        val myproduct__name = itemView.txt_name_myproduct
        val myproduct__des = itemView.txt_desc_myproduct
        val myproduct_price = itemView.txt_price_myproduct
        val myproduct_img = itemView.img_myproduct
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.card3, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.myproduct__name.text = data[position].product_name
        holder.myproduct__des.text = data[position].product_des
        Picasso.get().load("${Url.api_img}storage/" + data[position].product_image)
            .error(R.drawable.img).into(holder.myproduct_img)

        holder.myproduct_price.text = data[position].product_price

        holder.card.setOnClickListener {
            val editor: SharedPreferences.Editor =
                activity.getSharedPreferences("sendmyProductDetails", Context.MODE_PRIVATE).edit()
            editor.putString("MyproductNid", data[position].new_id)
            editor.putString("MyproductNcatId", data[position].product_cat)
            editor.putString("MyproductNname", data[position].product_name)
            editor.putString("MyproductNImg", data[position].product_image)
            editor.putString("MyproductNPrice", data[position].product_price)
            editor.putString("MyproductNDesc", data[position].product_des)
            editor.putString("from", "Myproduct")
            editor.apply()

            val i = Intent(this.activity, EditDetialsActivity::class.java)
            activity.startActivity(i)
        }
        holder.next.setOnClickListener {
            val i = Intent(this.activity, EditDetialsActivity::class.java)
            activity.startActivity(i)
        }

    }


}
