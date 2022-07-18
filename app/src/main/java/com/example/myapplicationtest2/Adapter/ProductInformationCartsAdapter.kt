package com.example.myapplicationtest2.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationprojecttender.model.product
import com.example.myapplicationprojecttender.model.productcarft
import com.example.myapplicationprojecttender.model.productinformationcarft
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cardrequest.view.*
import kotlinx.android.synthetic.main.category_layout.view.*

class ProductInformationCartsAdapter(
    var activity: Activity,
    var data: MutableList<productinformationcarft>
) :
    RecyclerView.Adapter<ProductInformationCartsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cat_img = itemView.imageView12
        val textView19 = itemView.textView19
        val button8 = itemView.button8
//        val cat_name = itemView.test

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.cardrequest, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView19.text = data[position].order_name
        Picasso.get().load("${Url.api_img}storage/" + data[position].order_img)
            .error(R.drawable.img).into(holder.cat_img)


        var playList = mutableListOf<productinformationcarft>()

    }
}
