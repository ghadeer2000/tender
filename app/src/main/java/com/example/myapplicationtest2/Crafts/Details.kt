package com.example.myapplicationtest2.Crafts

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.example.myapplicationtest2.Adapter.SavedAdapter
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account.view.*
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.txt_price
import kotlinx.android.synthetic.main.activity_details.view.*
import kotlinx.android.synthetic.main.activity_detials.*
import java.io.ByteArrayOutputStream
import java.io.InputStream

class Details : AppCompatActivity() {
    lateinit var productId: String
    lateinit var catId: String
    lateinit var img: String
    var tokinCraft: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        img = ""
        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        btn_done.setOnClickListener {
            updatemyproduct()
            val i = Intent(this, MyProduct::class.java)
            startActivity(i)
        }
        btn_delete.setOnClickListener {
            deleteMyProduct(productId)
            val i = Intent(this, MyProduct::class.java)
            startActivity(i)
        }


        image_myproduct.setOnClickListener {
            PickImageDialog.build(PickSetup())
                .setOnPickResult { r ->
                    val inputStream: InputStream? = this.contentResolver.openInputStream(r.uri)
                    val image = BitmapFactory.decodeStream(inputStream)
                    img = sendImage(image)
                    image_myproduct.setImageBitmap(r.bitmap)
                }
                .setOnPickCancel {
                }.show(this)
        }
        val prefs: SharedPreferences = this.getSharedPreferences(
            "sendmyProductDetails",
            Context.MODE_PRIVATE
        )
        val from = prefs.getString("from", null)


        if (from == "Myproduct") {
            productId = prefs.getString("MyproductNid", null)!!
            catId = prefs.getString("MyproductNcatId", null)!!
            val name = prefs.getString("MyproductNname", null)
            val img = prefs.getString("MyproductNImg", null)
            val desc = prefs.getString("MyproductNDesc", null)
            val price = prefs.getString("MyproductNPrice", null)


            Picasso.get().load("${Url.api_img}storage/" + img)
                .error(R.drawable.img).into(image_myproduct)

            textInputLayout.txt_product_name.setText(name)
            textInputLayout2.txt_price.setText(price)
            textInputLayout3.text_dec.setText(desc)
        }
    }

    private fun updatemyproduct() {
        val url = Url.api + "seller/products/update"
        val token = MainActivity2.tokinCraft
        val queue = Volley.newRequestQueue(this)
        val request = @SuppressLint("SetTextI18n")
        object : VolleyMultipartRequest(
            Request.Method.POST, url,
            Response.Listener { response ->

            },
            Response.ErrorListener { error ->
                Log.e("error Listener", "${error.message}")
            }) {


            override fun getParams(): MutableMap<String, String> {
                var params = HashMap<String, String>()
                params["id"] = productId
                params["category_id"] = catId
                params["name"] = textInputLayout.txt_product_name.text.toString()
                params["price"] = textInputLayout2.txt_price.text.toString()
                params["description"] = textInputLayout3.text_dec.text.toString()
                params["image"] = img

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

    private fun deleteMyProduct(id:String){
        val url = Url.api + "seller/products/delete"
        val token = MainActivity2.tokinCraft
        val queue= Volley.newRequestQueue(this)
        val request = object : StringRequest(
            Request.Method.POST,url,
            Response.Listener { response ->

                Log.e("deleteAddress","$response")


            },
            Response.ErrorListener { error ->
                Log.e("deleteAddress","${error.message}")

            }){
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


    private fun sendImage(image: Bitmap?): String {
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
