package com.example.myapplicationtest2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main2.*
import com.example.myapplicationtest2.R
import com.example.myapplicationtest2.fragmentCrafts.MainActivity2
import com.example.myapplicationtest2.fragmentCrafts.ProfileFragmentCrafts

class MainActivity2 : AppCompatActivity() {
    companion object {
        var tokinbuyer : String =""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var sharedPreferences = getSharedPreferences("buyer", AppCompatActivity.MODE_PRIVATE)
        MainActivity2.tokinCraft = sharedPreferences.getString("token","token").toString()

        replaceFragment(HomeFragment())
        bottom_navigation.setItemSelected(R.id.ic_home)

        bottom_navigation.setOnItemSelectedListener {
            when (it) {
                R.id.ic_home -> replaceFragment(HomeFragment())
                R.id.ic_profile -> replaceFragment(ProfileFragment())
                R.id.ic_cart -> replaceFragment(CartFragment())
                R.id.ic_craftsmen -> replaceFragment(CraftsmenFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}
