package com.example.myapplicationtest2.Crafts

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplicationprojecttender.model.follower
import com.example.myapplicationtest2.Adapter.FollowersAdapter
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import kotlinx.android.synthetic.main.activity_followers_seller.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class FollowersActivity : AppCompatActivity() {
    lateinit var playList: MutableList<follower>
    lateinit var playListfollower: MutableList<follower>
    var followingnum: String = ""
    var followernum: String = ""
    var tokinCraft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followers)
        playList = mutableListOf<follower>()
        playListfollower = mutableListOf<follower>()
        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()


        if (intent.getStringExtra("from") == "following") {
            GetFollower()

        } else if (intent.getStringExtra("from") == "follower") {
            GetFollowing()

        }else if (intent.getStringExtra("from") == "follow") {
            GetFollowing()

        }


    }

    fun GetFollowing() {
        val url = Url.api + "seller/following"
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

                    followernum = "${playList.size}"


                    val followersAdapter = FollowersAdapter(this, playList)
                    recycler_follower.adapter = followersAdapter
                    recycler_follower.layoutManager = LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL, false
                    )

                    recycler_follower.setHasFixedSize(true)
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

    fun GetFollower() {
        val url = Url.api + "seller/followers"
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

                    val data = response.getJSONArray("followers")

                    Log.e("Allfollowers", "$data")

                    for (i in 0 until data.length()) {
                        val case = data.getJSONObject(i)
                        playListfollower.add(
                            follower(
                                case.getString("id"),
                                case.getString("image"),
                                case.getString("name"),
                                case.getString("craft_name")
                            )
                        )


                    }
                    Log.e("Allfollowers", "$playList")

                    followingnum = "${response.getString("followersCount")}"

                    val followersAdapter = FollowersAdapter(this, playListfollower)
                    recycler_follower.adapter = followersAdapter
                    recycler_follower.layoutManager = LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL, false
                    )

                    recycler_follower.setHasFixedSize(true)
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
