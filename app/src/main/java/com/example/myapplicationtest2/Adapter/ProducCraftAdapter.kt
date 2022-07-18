package com.example.myapplicationtest2.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationprojecttender.model.product
import com.example.myapplicationprojecttender.model.productcarft
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_layout.view.*
import kotlinx.android.synthetic.main.productcarft.view.*

class ProducCraftAdapter(var activity: Activity, var data: MutableList<productcarft>) :
    RecyclerView.Adapter<ProducCraftAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val product_craft_img = itemView.product_craft_img
//        val cat_name = itemView.test

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.productcarft, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.cat_name.text = data[position].catrgory_name
        Picasso.get().load("${Url.api_img}storage/" + data[position].product_image)
            .error(R.drawable.img).into(holder.product_craft_img)

    }
}
