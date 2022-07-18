package com.example.myapplicationtest2.Crafts

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.jasaraapplication.VolleyMultipartRequest
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account.view.*
import kotlinx.android.synthetic.main.fragment_profile_crafts.view.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream

class Account : AppCompatActivity() {
    lateinit var id_user: String
    lateinit var phone: String
    lateinit var img: String
    lateinit var cPrefs: SharedPreferences
    var tokinCraft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
         cPrefs = this.getSharedPreferences(
            "profilePref",
            Context.MODE_PRIVATE
        )
        img = ""
        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        GetProfileInformation()
        btn_save.setOnClickListener {
            updateprofile()

            GetProfileInformation()

        }
        btn_image.setOnClickListener {
            PickImageDialog.build(PickSetup())
                .setOnPickResult { r ->
                    val inputStream: InputStream? = this.contentResolver.openInputStream(r.uri)
                    val image = BitmapFactory.decodeStream(inputStream)
                     img = sendImage(image)
                    profile_img.setImageBitmap(r.bitmap)
                }
                .setOnPickCancel {
                }.show(this)
        }
    }
    private fun GetProfileInformation() {
//        val url = Url.api + "localhost:8000/api/seller/userProfile"
        val url = Url.api + "seller/userProfile"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)
                id_user = data.getString("id")
                name_user.txt_name!!.setText(data.getString("name"))
//                email.txt_email.setText(data.getString("email"))
                Phone.txt_phone.setText(data.getString("phone_number"))
                var email = data.getString("email")
                var list = email.split("@")
                var e = list.first()
                Name.txt_username.setText(e)
                CraftName.txt_craftname.setText(data.getString("craft_name"))
                phone = data.getString("phone_number")
                Log.e("data", "$data")
                var img = data.getString("image")
                // Log.e("data", "$data")

                Picasso.get().load("${Url.api_img}storage/$img").error(R.drawable.conan).into(profile_img)
//                Picasso.get().load("${Url.api_img}storage/$img").into(profile_img)

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

    private fun updateprofile() {
        val url = Url.api + "seller/updateProfile"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : VolleyMultipartRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
//                val data = JSONObject(response)
                Snackbar.make(mainlayout,"تم تعديل البيانات بنجاح", Snackbar.LENGTH_LONG).show()

            },
            Response.ErrorListener { error ->
                Log.e("error Listener", "${error.message}")
            }) {


            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
//                params["id"] = Id
                params["name"] = name_user.txt_name.text.toString()
                params["craft_name"] = CraftName.txt_craftname.text.toString()
                params["phone_number"] = Phone.txt_phone.text.toString()

                return params
            }
            override fun getByteData(): Map<String, DataPart>? {
                val params: MutableMap<String, DataPart> = HashMap()
                params["image"] = DataPart(
                    "file_avatar.jpg",
                    getBitmapFromString(img),
                    "image/jpeg"
                )
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

    private fun sendImage(image: Bitmap?):String {
        val outputStream = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        return android.util.Base64.encodeToString(
            outputStream.toByteArray(),
            android.util.Base64.DEFAULT
        )


    }

    private fun getBitmapFromString(image: String): ByteArray {
        val bytes: ByteArray = android.util.Base64.decode(image, android.util.Base64.DEFAULT)
        return bytes
    }
//    private fun updateprofile(Id: String) {
//        val url = Url.api + "seller/updateProfile"
//        val token = Url.tokinSeller
//        val queue = Volley.newRequestQueue(this)
//        val request = object : StringRequest(
//            Request.Method.POST, url,
//            Response.Listener { response ->
//                val data = JSONObject(response)
//                Log.e("updateProfile", "$data")
//
//
//            },
//            Response.ErrorListener { error ->
//                Log.e("error Listener", "${error.message}")
//            }) {
//            override fun getParams(): MutableMap<String, String> {
//                var params = HashMap<String, String>()
//                params["seller_id"] = Id
//                return params
//
//                return params
//            }
//
//            override fun getHeaders(): MutableMap<String, String> {
//                val headers =
//                    HashMap<String, String>()
//                headers["Accept"] = "application/json"
//                headers["Authorization"] = "Bearer" + token
//                return headers
//            }
//
//
//        }
//
//
//
//        queue.add(request)
//
//    }

}
