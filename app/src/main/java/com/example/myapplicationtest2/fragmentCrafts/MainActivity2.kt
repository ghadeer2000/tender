package com.example.myapplicationtest2.fragmentCrafts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplicationtest2.Crafts.Account
import com.example.myapplicationtest2.Login.Welcome
import com.example.myapplicationtest2.R
import kotlinx.android.synthetic.main.activity_main_crafts2.*

class MainActivity2 : AppCompatActivity() {
    companion object {
         var tokinCraft: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_crafts2)

        var sharedPreferences = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        tokinCraft = sharedPreferences.getString("token", "").toString()
        Log.e("no", tokinCraft)

        val handler = Handler()
        handler.postDelayed(Runnable {
            val pref = getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().commit()
            val buyerPref = getSharedPreferences("buyer", Context.MODE_PRIVATE)
            val userPref = getSharedPreferences("user", Context.MODE_PRIVATE)
            val editor = buyerPref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            when {
                userPref.getBoolean("isLoggedIn", false) -> {
//                    startActivity(Intent(this, HomeCraftActivity::class.java))
//                    finish()
                }
                buyerPref.getBoolean("isLoggedIn", false) -> {
//                    startActivity(Intent(this, HomeFragmentCrafts::class.java))
//                    finish()
                }
                else -> {
//                    startActivity(Intent(this, Welcome::class.java))
//                    finish()
                }
            }
        }, 1500)


        replaceFragment(HomeFragmentCrafts())
        bottom_navigation2.setItemSelected(R.id.ic_home)

        bottom_navigation2.setOnItemSelectedListener {
            when (it) {
                R.id.ic_home -> replaceFragment(HomeFragmentCrafts())
                R.id.ic_profile -> replaceFragment(ProfileFragmentCrafts())
                R.id.ic_add -> replaceFragment(AddProductImageFragment())
                R.id.ic_cart -> replaceFragment(CartFragmentCrafts())
                R.id.ic_craftsmen -> replaceFragment(CraftsmenFragmentCrafts())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container2, fragment)
        transaction.commit()
    }
}
