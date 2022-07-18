package com.example.myapplicationtest2.Crafts

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplicationprojecttender.model.category
import com.example.myapplicationprojecttender.model.myproduct
import com.example.myapplicationprojecttender.model.newproduct
import com.example.myapplicationtest2.Adapter.NewProductAdapter
import com.example.myapplicationtest2.Adapter.ProfileCraftProductAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_carft_craft_profile.*
import kotlinx.android.synthetic.main.activity_carft_craft_profile.btn_follow
import kotlinx.android.synthetic.main.activity_carft_craft_profile.profile_image
import kotlinx.android.synthetic.main.activity_carft_craft_profile.txt_name_craft
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class CraftCraftProfile : AppCompatActivity() {
    lateinit var playList: MutableList<newproduct>
    var numpost: String = ""
    var follower: String = ""
    var following: String = ""
    var name_craft: String = ""
    var craftname: String = ""
    var craftimg: String = ""
    var fromid: String = ""
    var userid: String = ""
    lateinit var status: String
    var tokinCraft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carft_craft_profile)
        status = "follow"
        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        playList = mutableListOf<newproduct>()
        val prefs: SharedPreferences = this.getSharedPreferences(
            "sendCraftProductDetails",
            Context.MODE_PRIVATE
        )
        fromid = prefs.getString("Cid", null).toString()

        GetSellerProduct()

        btn_follow.setOnClickListener {
            follows(userid)
            if (status == "unfollow") {
                btn_follow.text = "إلغاء المتابعة"
            } else {
                btn_follow.text = " متابعة"

            }
        }
    }

    fun GetSellerProduct() {
        val url = Url.api + "seller/getSellers"
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

                    val data = response.getJSONArray("sellers")


                    Log.e("SavedProducts", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        var caseproducts = case.getJSONArray("products")
                        for (e in 0 until caseproducts.length()) {
                            val caseproduct = caseproducts.getJSONObject(e)
                            if (case.getString("id") == fromid) {
                                playList.add(
                                    newproduct(
                                        caseproduct.getString("id"),
                                        case.getString("id"),
                                        caseproduct.getString("category_id"),
                                        caseproduct.getString("image"),
                                        caseproduct.getString("name"),
                                        caseproduct.getString("description"),
                                        caseproduct.getString("price")
                                    )
                                )
                                craftimg = case.getString("image")
                                Picasso.get().load("${Url.api_img}storage/$craftimg")
                                    .error(R.drawable.img).into(profile_image)

                                numpost = case.getString("number of products")
                                follower = case.getString("number of followers")
                                following = case.getString("number of following")
                                craftname = case.getString("craft_name")
                                name_craft = case.getString("name")
                                userid = case.getString("id")
                                txt_num_post.text = numpost
                                num_follower.text = follower
                                num_following.text = following
                                txt_name_profile.text = name_craft
                                username.text = name_craft
                                txt_name_craft.text = craftname
                            }
                        }


                    }

                    val newProductAdapter = ProfileCraftProductAdapter(this, playList)
                    recycler_post.adapter = newProductAdapter

                    recycler_post.layoutManager = GridLayoutManager(this, 2)


                    recycler_post.setHasFixedSize(true)
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

    private fun follows(seller_id: String) {
        val url = Url.api + "seller/follow"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)
                var state = data.getString("message")
                if (state == "unfollow") {
                    status = "unfollow"
                } else {
                    status = "follow"

                }
                Log.e("error Listener", "${data}")


            },
            Response.ErrorListener { error ->
                Log.e("error Listener", "${error.message}")
            }) {


            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params["seller_id"] = seller_id
                return params
            }


            override fun getHeaders(): MutableMap<String, String> {
                val headers =
                    HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer $token"
                return headers
            }


        }

        queue.add(request)

    }

}
