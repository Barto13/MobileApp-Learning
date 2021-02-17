package com.barto.simplecrud

import android.content.Context
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.barto.simplecrud.databinding.ActivityMainBinding
import android.content.Intent;
import android.content.SharedPreferences
import android.util.TypedValue
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;
    private lateinit var sp: SharedPreferences
    private lateinit var sp2: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        sp = getSharedPreferences(
                "Theme", Context.MODE_PRIVATE
        )
        sp2 = getSharedPreferences(
                "Font", Context.MODE_PRIVATE
        )

        val theme1 = sp.getString("themeName", "default")
        val font1 = sp2.getString("fontSize1", "default")

        when(theme1){
            "Red" -> theme.applyStyle(R.style.ThemeRed, true)
            "Green" -> theme.applyStyle(R.style.ThemeGreen, true)
            "Default" -> theme.applyStyle(R.style.Theme_SimpleCRUD, true)
        }

        binding.test.text = theme1
        binding.tv1.text = FirebaseAuth.getInstance().currentUser?.email

        setContentView(binding.root)

        when(font1){
            "FontSizeSmall" -> test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            "FontSizeLarge" -> test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
            "FontSizeDefault" -> test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        }

        binding.button1.setOnClickListener {
            val intent1 = Intent(this, ListActivity::class.java)
            intent1.putExtra("user", binding.tv1.text)
            startActivity(intent1)
        }

        binding.buttonOptions.setOnClickListener{
            val intent2 = Intent(this, OptionsActivity::class.java)
            startActivity(intent2)
        }

        binding.btLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "User logged out! ", Toast.LENGTH_SHORT).show()
            val intent2 = Intent(this, LoginActivity::class.java)
            startActivity(intent2)
        }

        binding.btMaps.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        binding.btShopList.setOnClickListener{
            val intent = Intent(this, ShopListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        sp = getSharedPreferences(
                "Theme", Context.MODE_PRIVATE
        )
        sp2 = getSharedPreferences(
                "Font", Context.MODE_PRIVATE
        )

        val theme1 = sp.getString("themeName", "default")
        binding.test.text = theme1

    }


}