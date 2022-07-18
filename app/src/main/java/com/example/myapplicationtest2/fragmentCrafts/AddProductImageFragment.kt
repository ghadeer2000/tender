package com.example.myapplicationtest2.fragmentCrafts

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.Url
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.first_add_crafts.view.*
import kotlinx.android.synthetic.main.first_add_crafts.view.add_image
import kotlinx.android.synthetic.main.fragment_add_product_image.view.*
import java.io.ByteArrayOutputStream
import java.io.InputStream

class AddProductImageFragment : Fragment() {

    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_product_image, container, false)

        root.add_image.setOnClickListener {
            PickImageDialog.build(PickSetup())
                .setOnPickResult { r ->
                    val inputStream: InputStream? =
                        requireActivity().contentResolver.openInputStream(r.uri)
                    val image = BitmapFactory.decodeStream(inputStream)
                    var img = sendImage(image)
                    root.add_image.setImageBitmap(r.bitmap)
                    val editor: SharedPreferences.Editor =
                        requireActivity().getSharedPreferences(
                            "sendImageProductDetails",
                            Context.MODE_PRIVATE
                        )
                            .edit()
                    editor.putString("image", "$img")
                    editor.apply()

                }
                .setOnPickCancel {
                }.show(requireActivity().supportFragmentManager)
        }

        root.button_next_img.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container2,
                AddProductDetailsFragment()
            ).commit()
        }


        return root
    }

    private fun sendImage(image: Bitmap?) {
        val outputStream = ByteArrayOutputStream()
        image!!.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        var base64String = android.util.Base64.encodeToString(
            outputStream.toByteArray(),
            android.util.Base64.DEFAULT
        )

    }
}
