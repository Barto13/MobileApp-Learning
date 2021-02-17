package com.barto.simplecrud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.barto.simplecrud.databinding.ActivityOptionsBinding
import kotlinx.android.synthetic.main.activity_options.*


class OptionsActivity : AppCompatActivity(){

    private lateinit var binding: ActivityOptionsBinding
    private lateinit var sp: SharedPreferences
    private lateinit var sp2: SharedPreferences
    val themeName = "currentTheme"
    val fontSize1 = "currentFont"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsBinding.inflate(layoutInflater)

        sp = getSharedPreferences(
                "Theme", Context.MODE_PRIVATE
        )
        sp2 = getSharedPreferences(
                "Font", Context.MODE_PRIVATE
        )

        when(sp.getString("themeName", "default")){
            "Red" -> theme.applyStyle(R.style.ThemeRed, true)
            "Green" -> theme.applyStyle(R.style.ThemeGreen, true)
            "Default" -> theme.applyStyle(R.style.Theme_SimpleCRUD, true)
        }

        setContentView(binding.root)


        when(sp2.getString("fontSize1", "default")){
            "FontSizeSmall" -> tv_fontDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
            "FontSizeLarge" -> tv_fontDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32F)
            "FontSizeDefault" -> tv_fontDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
        }

        binding.btRed.setOnClickListener{
            binding.tvChoice.text = "Red"
            sp.edit().putString(themeName, "Red")
        }

        binding.btGreen.setOnClickListener{
            binding.tvChoice.text = "Green"
            sp.edit().putString(themeName, "Green")
        }

        binding.buttonDefaultValues.setOnClickListener{
            binding.tvChoice.text = "Default"
            binding.tvChoiceFont.text = "FontSizeDefault"
            sp.edit().putString(themeName, "Default")
            sp2.edit().putString(fontSize1, "FontSizeDefault")
        }

        binding.font1.setOnClickListener{
            binding.tvChoiceFont.text = "FontSizeSmall"
            sp2.edit().putString(fontSize1, "FontSizeSmall")
        }

        binding.font2.setOnClickListener{
            binding.tvChoiceFont.text = "FontSizeLarge"
            sp2.edit().putString(fontSize1, "FontSizeLarge")
        }

        binding.buttonSave.setOnClickListener{
        val intent = intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            val editor = sp.edit()
            val editor2 = sp2.edit()
            editor.putString("themeName", binding.tvChoice.text.toString())
            editor2.putString("fontSize1", binding.tvChoiceFont.text.toString())
            editor2.apply()
            editor2.commit()
            editor.apply()
            editor.commit()
            finish()
            startActivity(intent)
        }
    }

    override fun onStart(){
        super.onStart()
        binding.tvChoice.text = sp.getString("themeName", "default")
        binding.tvChoiceFont.text = sp2.getString("fontSize1", "default")

    }


}