package com.example.myapplicationtest2.Adapter

import android.app.Activity
import android.app.AlertDialog
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
import com.example.myapplicationprojecttender.model.cart
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.reques_item.view.*

class CartSellAdapter(var activity: Activity, var data: MutableList<cart>) :
    RecyclerView.Adapter<CartSellAdapter.MyViewHolder>() {
    var name_result = ""

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.cart_card
        val lineardelete = itemView.lineardelete
        val cart_name = itemView.cart_name
        val cart_count = itemView.cart_count
        val cart_img = itemView.cart_imvCircular
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.reques_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cart_name.text = data[position].cart_name
        Picasso.get().load("${Url.api_img}storage/" + data[position].cart_img)
            .error(R.drawable.img).into(holder.cart_img)
        var tokinCraft: String = ""
        var sharedPreferences =
            activity.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        holder.cart_count.text = data[position].cart_count


        if (name_result == "yes") {
//            deleteProductCart(data[position].id!!,position,holder)

        }
        holder.lineardelete.setOnClickListener {
            deleteItem()
            deleteProductCart(data[position].id!!, position, holder, tokinCraft)
        }


    }

    fun deleteProductCart(id: String, position: Int, holder: MyViewHolder, tokinCraft: String) {
        val url = Url.api + "seller/purchaseCart/delete"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(activity)
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->

                Log.e("deleteAddress", "$response")
                data.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(holder.adapterPosition, data.size)

            },
            Response.ErrorListener { error ->
                Log.e("deleteAddress", "${error.message}")

            }) {
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

    fun deleteItem() {
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this item ?")
        alertBuilder.setPositiveButton("Delete") { _, _ ->
//            if(::CartSellAdapter.isInitialized){
//                myAdapter.deleteSelectedItem()
//                showHideDelete(false)
            name_result = "yes"
            Toast.makeText(activity, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show()

        }

        alertBuilder.setNegativeButton("No") { _, _ ->

        }

        alertBuilder.setNeutralButton("Cancel") { _, _ ->

        }
        alertBuilder.show()
    }


}
