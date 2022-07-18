package com.example.myapplicationtest2.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationprojecttender.model.product
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_layout.view.*

class CategoryAdapter(var activity: Activity, var data: MutableList<category>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cat_img = itemView.cat_img
//        val cat_name = itemView.test

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.category_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.cat_name.text = data[position].catrgory_name
        Picasso.get().load("${Url.api_img}storage/" + data[position].catrgory_image)
            .error(R.drawable.img).into(holder.cat_img)
       // Picasso.get().load("http://192.168.137.1:80/storage/app/public/images/" + data[position].product_img)
           // .error(R.drawable.img).into(holder.saved_img)
//        Picasso.get().load(R.drawable.img)
//         .error(R.drawable.img).into(holder.saved_img)
      //  holder.saved_img.setImageResource(data[position].product_img)
    }
}
