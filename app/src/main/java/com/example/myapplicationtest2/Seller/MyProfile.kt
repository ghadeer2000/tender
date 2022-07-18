package com.example.myapplicationtest2.Seller

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
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_my_profile.btn_save
import kotlinx.android.synthetic.main.activity_my_profile.view.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream

class MyProfile : AppCompatActivity() {
    lateinit var id_user: String
    lateinit var phone: String
    lateinit var img: String
    lateinit var cPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        cPrefs = this.getSharedPreferences(
            "profilePref",
            Context.MODE_PRIVATE
        )
        img = ""

        GetProfileInformation()
        btn_save.setOnClickListener {
            updateprofile()

            GetProfileInformation()

        }
        btn_add_img.setOnClickListener {
            PickImageDialog.build(PickSetup())
                .setOnPickResult { r ->
                    val inputStream: InputStream? = this.contentResolver.openInputStream(r.uri)
                    val image = BitmapFactory.decodeStream(inputStream)
                    img = sendImage(image)
                    profile_image.setImageBitmap(r.bitmap)
                }
                .setOnPickCancel {
                }.show(this)
        }
    }
    private fun GetProfileInformation() {
//        val url = Url.api + "localhost:8000/api/seller/userProfile"
        val url = Url.api + "buyer/userProfile"
        val token = com.example.myapplicationtest2.MainActivity2.tokinbuyer
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                val data = JSONObject(response)
                id_user = data.getString("id")
                editTextPhone.txt_name!!.setText(data.getString("name"))
//                email.txt_email.setText(data.getString("email"))
                editTextPhone1.txt_phone.setText(data.getString("phone_number"))
                var email = data.getString("email")
                var list = email.split("@")
                var e = list.first()
                editTextPhone2.txt_username.setText(e)
                phone = data.getString("phone_number")
                Log.e("data", "$data")
                var img = data.getString("image")
                // Log.e("data", "$data")

                Picasso.get().load("${Url.api_img}storage/$img").error(R.drawable.conan).into(profile_image)
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
        val url = Url.api + "buyer/updateProfile"
        val token = com.example.myapplicationtest2.MainActivity2.tokinbuyer
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
                params["name"] = editTextPhone.txt_name.text.toString()
                params["phone_number"] = editTextPhone1.txt_phone.text.toString()

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
}
