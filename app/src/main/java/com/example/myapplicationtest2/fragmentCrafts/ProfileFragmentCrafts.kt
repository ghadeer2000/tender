package com.example.myapplicationtest2.fragmentCrafts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import com.example.myapplicationtest2.Crafts.*
import com.example.myapplicationtest2.Login.Welcome
import com.example.myapplicationtest2.MySingleton
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Seller.FollowersActivitySeller
import com.example.myapplicationtest2.SellerLogin.SignInSeller
import com.example.myapplicationtest2.Url
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_followers_seller.*
import kotlinx.android.synthetic.main.activity_saved_product.*
import kotlinx.android.synthetic.main.fragment_profile_crafts.*
import kotlinx.android.synthetic.main.fragment_profile_crafts.profile_img
import kotlinx.android.synthetic.main.fragment_profile_crafts.view.*
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class ProfileFragmentCrafts : Fragment() {

    lateinit var playListproduct: MutableList<product>
    lateinit var root: View
    lateinit var id: String
    lateinit var playList: MutableList<follower>
    lateinit var playListfollower: MutableList<follower>
    var tokinCraft: String = ""
    var followingnum: String = ""
    var followernum: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_profile_crafts, container, false)
        playListproduct = mutableListOf<product>()
        playList = mutableListOf<follower>()
        playListfollower = mutableListOf<follower>()
        var sharedPreferences = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        Log.e("data", "$tokinCraft")

        GetProfileInformation()
        GetFollowing()
        GetFollower()

        root.acountSetting.setOnClickListener {
            val i = Intent(this.context, Account::class.java)
            startActivity(i)
        }
        root.text_myorder.setOnClickListener {
            val i = Intent(this.context, CraftMyRequestActivity::class.java)
            startActivity(i)
        }
        root.saved.setOnClickListener {
            val i = Intent(this.context, Saved_Product::class.java)
            startActivity(i)
        }
        root.myadreess.setOnClickListener {
            val i = Intent(this.context, Address::class.java)
            startActivity(i)
        }
        root.follow.setOnClickListener {
            val i = Intent(this.context, CraftFollowerActivity::class.java)
            i.putExtra("from", "follow")
            startActivity(i)
        }
        root.my_product.setOnClickListener {
            val i = Intent(this.context, MyProduct::class.java)
            startActivity(i)
        }

        root.log_out.setOnClickListener {
            var sharedPreferences = this.requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            LogOutSeller()
            val i = Intent(this.context, SignInSeller::class.java)
            startActivity(i)

        }
        root.profile_follownum.setOnClickListener {
            val i = Intent(this.context, CraftFollowerActivity::class.java)
            i.putExtra("from", "follower")
            startActivity(i)

        }
        root.profile_followingnum.setOnClickListener {
            val i = Intent(this.context, CraftFollowerActivity::class.java)
            i.putExtra("from", "following")
            startActivity(i)

        }


        return root


    }

    private fun GetProfileInformation() {
        val url = Url.api + "seller/userProfile"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(context)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)
                root.profile_name.text = data.getString("name")
                root.profile_username.text = data.getString("craft_name")
                var img = data.getString("image")

                Picasso.get().load("${Url.api_img}storage/$img").error(R.drawable.wemon).into(profile_img)
                var name = data.getString("name")
                id = data.getString("id")
                Log.e("data", "$data")

            },
            Response.ErrorListener { error ->
                Log.e("error Listener", "${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()

                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers =
                    HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer" + token
                return headers
            }


        }



        queue.add(request)

    }


    private fun LogOutSeller() {
        val url = Url.api + "seller/logout"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(context)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)

                Log.e("data", "$data")

            },
            Response.ErrorListener { error ->
                Log.e("error Listener", "${error.message}")
            }) {
            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()

//                params["device_token"] = deviceToken
                return params
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers =
                    HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer" + token
                return headers
            }


        }



        queue.add(request)

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

                    followingnum = "${playList.size}"
                    Log.e("Followingnum", "$followingnum")
                    root.profile_follownum.setText(followingnum)


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
                    Log.e("playListfollower", "$playListfollower")

//                    followingnum = "${response.getString("followersCount")}"
                    followernum = "${playListfollower.size}"
                    root.profile_followingnum.setText(followernum)

                    Log.e("Followernum", "$followingnum")



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
