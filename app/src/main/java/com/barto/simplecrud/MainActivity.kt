package com.barto.simplecrud

import android.content.Context
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.barto.simplecrud.databinding.ActivityMainBinding
import android.content.Intent;
import android.content.SharedPreferences
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_options.*

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

        setContentView(binding.root)

//        test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
        when(font1){
            "FontSizeSmall" -> test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            "FontSizeLarge" -> test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
            "FontSizeDefault" -> test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        }


        binding.button1.setOnClickListener {
            val intent1 = Intent(this, SecondaryActivity::class.java)
            startActivity(intent1)
        }

        binding.buttonOptions.setOnClickListener{
            val intent2 = Intent(this, OptionsActivity::class.java)
            startActivity(intent2)
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
//        val font1 = sp2.getString("fontSize1", "default")

        binding.test.text = theme1

    }


}