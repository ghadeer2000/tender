package com.example.myapplicationtest2.fragmentCrafts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.SellerLogin.SignInSeller
import kotlinx.android.synthetic.main.fragment_success_add_product.view.*

class SuccessAddProductFragment : Fragment() {
    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_success_add_product, container, false)
        root.publicnewproduct.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container2,
                HomeFragmentCrafts()
            ).commit()
        }
        return root
    }
}
