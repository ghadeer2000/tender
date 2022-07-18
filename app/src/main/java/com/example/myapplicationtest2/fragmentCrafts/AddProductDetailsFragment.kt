package com.example.myapplicationtest2.fragmentCrafts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.jasaraapplication.VolleyMultipartRequest
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationtest2.Adapter.CategoryAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account.view.*
import kotlinx.android.synthetic.main.fragment_add_product_details.view.*
import kotlinx.android.synthetic.main.fragment_home_crafts.view.*
import kotlinx.android.synthetic.main.fragment_success_add_product.view.*
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class AddProductDetailsFragment : Fragment() {

    lateinit var root: View
    lateinit var cat_id_product: String
    lateinit var playListCategory: MutableList<String>
    var tokinCraft: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_product_details, container, false)
        playListCategory = mutableListOf<String>()
        cat_id_product = ""
        var sharedPreferences = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        var name = root.editTextTextPersonName.text
        var price = root.editTextTextPersonPrice.text
        var desc = root.editTextTextPersondesc.text

        root.button.setOnClickListener {
            addtoMyProduct()
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container2,
                SuccessAddProductFragment()
            ).commit()
        }
        getCategories()
        return root
    }

    fun getCategories() {
        val url = Url.api + "seller/categories"
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

                    val data = response.getJSONArray("categories")
                    var catId = mutableListOf<String>()
                    Log.e("categories", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        playListCategory.add(case.getString("id"))
                        catId.add(case.getString("name"))


                    }
                    val cityAdapter = ArrayAdapter(
                        this.requireActivity(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        catId
                    )
                    root.spinnerCity.adapter = cityAdapter
                    root.spinnerCity.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {

                                var playListcategory = playListCategory[position]
                                cat_id_product = playListCategory[position]
                                Log.e("city_id", playListcategory)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }

                        }

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

    private fun addtoMyProduct() {
        val url = Url.api + "seller/products/create"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(activity)
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                Log.e("add toSaved", response)
                val data = JSONObject(response)
                var message = data.getString("message")

                Log.e("addtoMyProduct", "${data}")

            },
            Response.ErrorListener { error ->
                Log.e("addtoMyProduct", "${error.message}")

            }) {

            override fun getParams(): MutableMap<String, String> {

                var params = HashMap<String, String>()
                params["image"] = "1656260290.jpeg"
                params["name"] = root.editTextTextPersonName.text.toString()
                params["description"] = root.editTextTextPersondesc.text.toString()
                params["price"] = root.editTextTextPersonPrice.text.toString()
                params["category_id"] = cat_id_product

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
//    private fun updateprofile(Id: String) {
//        val url = Url.api + "seller/updateProfile"
//        val token = Url.tokinSeller
//        val queue = Volley.newRequestQueue(this)
//        val request = @SuppressLint("SetTextI18n")
//        object : VolleyMultipartRequest(
//            Request.Method.POST, url,
//            Response.Listener { response ->
////                val data = JSONObject(response)
//                Snackbar.make(mainlayout,"تم تعديل البيانات بنجاح", Snackbar.LENGTH_LONG).show()
//
//            },
//            Response.ErrorListener { error ->
//                Log.e("error Listener", "${error.message}")
//            }) {
//
//
//            override fun getParams(): MutableMap<String, String> {
//                var params = HashMap<String, String>()
//                params["id"] = Id
//                params["name"] = name_user.txt_name.text.toString()
//                params["craft_name"] = CraftName.txt_craftname.text.toString()
//                params["phone_number"] = Phone.txt_phone.text.toString()
//
//                return params
//            }
//            override fun getByteData(): Map<String, DataPart>? {
//                val params: MutableMap<String, DataPart> = HashMap()
//                params["image"] = DataPart(
//                    "file_avatar.jpg",
//                    getBitmapFromString(img),
//                    "image/jpeg"
//                )
//                return params
//            }
//            override fun getHeaders(): MutableMap<String, String> {
//                val headers =
//                    HashMap<String, String>()
//                headers["Accept"] = "application/json"
//                headers["Authorization"] = "Bearer $token"
//                return headers
//            }
//
//
//        }
//
//        queue.add(request)
//
//    }


}
