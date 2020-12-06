package com.barto.simplecrud

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.barto.simplecrud.databinding.ActivitySecondaryBinding
import kotlinx.android.synthetic.main.activity_secondary.*
import kotlinx.android.synthetic.main.activity_secondary.view.*

class SecondaryActivity : AppCompatActivity(){

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivitySecondaryBinding.inflate(layoutInflater)

        sp = getSharedPreferences(
                "Theme", Context.MODE_PRIVATE
        )

        val theme1 = sp.getString("themeName", "default")

        when(theme1){
            "Red" -> theme.applyStyle(R.style.ThemeRed, true)
            "Green" -> theme.applyStyle(R.style.ThemeGreen, true)
            "Default" -> theme.applyStyle(R.style.Theme_SimpleCRUD, true)
        }



        setContentView(binding.root)
//        val intent1 = intent

        binding.rv1.layoutManager = LinearLayoutManager(this)

        binding.rv1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val viewModel = ProductViewModel(application)
        val adapter = ProductAdapter(viewModel)

        viewModel.allProducts.observe(this, Observer {
            it.let{
                adapter.setProductList(it)
            }
        })

        binding.rv1.adapter = adapter

        binding.button.setOnClickListener{
            viewModel.insert(Product(
                    name = binding.etProductName.text.toString(),
                    price = binding.etPrice.text.toString().toDouble(),
                    number = binding.etNumber.text.toString().toInt(),
                    isBought = binding.checkBox.isChecked
            ))
        }

        binding.button.setOnLongClickListener{
            viewModel.deleteAll()
            true
        }

        binding.btUpdate.setOnClickListener{
//            if(et_idToModify.text.toString() == adapter.Product(id).toString())
//            viewModel.update(Product())
            if(et_idToModify.text.toString().isNotEmpty()){
                viewModel.update(Product(
                        id = binding.etIdToModify.text.toString().toLong(),
                        name = binding.etProductName.text.toString(),
                        price = binding.etPrice.text.toString().toDouble(),
                        number = binding.etNumber.text.toString().toInt(),
                        isBought = binding.checkBox.isChecked
                ))

            }

        }
    }

}