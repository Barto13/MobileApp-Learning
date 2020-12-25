package com.barto.simplecrud

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.barto.simplecrud.databinding.ActivityListBinding
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_list.view.*

class ListActivity : AppCompatActivity(){

    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityListBinding.inflate(layoutInflater)

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
        val intent1 = intent

        binding.rv1.layoutManager = LinearLayoutManager(this)

        binding.rv1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val viewModel = ProductViewModel(application)
        val adapter = ProductAdapter(viewModel)


//        information passed from broadcast receiver after clicking notification
        var broadcastProductId = intent.getStringExtra("productid")
        var broadcastProductName = intent.getStringExtra("productname")
        var broadcastProductPrice = intent.getStringExtra("productprice")
        var broadcastProductNumber = intent.getStringExtra("productnumber")
        var broadcastProductIsBought = intent.getStringExtra("productisbought")
        binding.etIdToModify.setText(broadcastProductId)
        binding.etProductName.setText(broadcastProductName)
        binding.etPrice.setText(broadcastProductPrice)
        binding.etNumber.setText(broadcastProductNumber)
        if(broadcastProductIsBought == "1"){
            binding.checkBox.setChecked(true)
        }


        viewModel.allProducts.observe(this, Observer {
            it.let{
                adapter.setProductList(it)
            }
        })

        binding.rv1.adapter = adapter

        binding.button.setOnClickListener{
            val s = viewModel.insert(Product(
                    name = binding.etProductName.text.toString(),
                    price = binding.etPrice.text.toString().toDouble(),
                    number = binding.etNumber.text.toString().toInt(),
                    isBought = binding.checkBox.isChecked
            ))

            //sending broadcast with newly added product information
            val broadcast = Intent(getString(R.string.addProduct))
            broadcast.component = ComponentName("com.barto.receiver", "com.barto.receiver.MyReceiver")
            broadcast.putExtra("name", binding.etProductName.text.toString())
            broadcast.putExtra("productId", s.toString())
            broadcast.putExtra("productPrice", binding.etPrice.text.toString())
            broadcast.putExtra("productNumber", binding.etNumber.text.toString())
            broadcast.putExtra("productIsBought", binding.checkBox.isChecked.toString())
            if(binding.checkBox.isChecked){
                broadcast.putExtra("productIsBought", "1")
            }
            sendBroadcast(broadcast, "com.barto.simplecrud.MY_PERMISSION")

        }

        binding.button.setOnLongClickListener{
            viewModel.deleteAll()
            true
        }

        binding.btUpdate.setOnClickListener{
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