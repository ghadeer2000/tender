package com.example.myapplicationtest2.Seller

import android.annotation.SuppressLint
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
import com.example.myapplicationprojecttender.model.follower
import com.example.myapplicationprojecttender.model.product
import com.example.myapplicationtest2.Adapter.FollowersAdapter
import com.example.myapplicationtest2.Adapter.SavedAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import kotlinx.android.synthetic.main.activity_followers_seller.*
import kotlinx.android.synthetic.main.activity_saved_product.*
import kotlinx.android.synthetic.main.fragment_profile_crafts.view.*
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class FollowersActivitySeller : AppCompatActivity() {
    lateinit var playList: MutableList<follower>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers_seller)
        playList = mutableListOf<follower>()


        GetFollower()

        recycler_follower.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        recycler_follower.setHasFixedSize(true)
    }

    fun GetFollower() {
        val url = Url.api + "seller/following"
        val token = Url.tokinSeller

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

                    val data = response.getJSONArray("sellerFollowings")

                    Log.e("Allfollowers", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        playList.add(
                            follower(
                                case.getString("id"),
                                case.getString("image"),
                                case.getString("name"),
                                case.getString("craft_name")
                            )
                        )


                    }
                    Log.e("Allfollowers", "$playList")

                    val followersAdapter = FollowersAdapter(this, playList)
                    recycler_follower.adapter = followersAdapter

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


}
