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
import com.barto.simplecrud.databinding.ActivityPubliclistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class PublicListActivity : AppCompatActivity(){

    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityPubliclistBinding.inflate(layoutInflater)
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

        //firebase
        val user = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()
        val refUser = database.getReference("public/")
        val productRef = refUser.child("products")

        val list = arrayListOf<Product>()
        val adapter = ProductAdapter(this, list, productRef)
        binding.rv1.adapter = adapter

        val chosenId = intent.getStringExtra("chosenId")
        val chosenName = intent.getStringExtra("chosenName")
        val chosenPrice = intent.getStringExtra("chosenPrice")
        val chosenNumber = intent.getStringExtra("chosenNumber")
        val chosenIsBought = intent.getStringExtra("chosenIsBought")

        binding.etIdToModify.setText(chosenId)
        binding.etProductName.setText(chosenName)
        binding.etPrice.setText(chosenPrice)
        binding.etNumber.setText(chosenNumber)
        if(chosenIsBought == "true"){
            binding.checkBox.setChecked(true)
        }

        binding.btNextList.setOnClickListener{
            val intent3 = Intent(this, ListActivity::class.java)
            startActivity(intent3)
        }

        binding.button.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                if(user != null){
                    val id = refUser.push().key
                    val name = binding.etProductName.text.toString().trim()
                    val price = binding.etPrice.text.toString().trim().toDouble()
                    val number = binding.etNumber.text.toString().trim().toInt()
                    val isBought = binding.checkBox.isChecked
                    val product = Product(id, name, price, number, isBought)
                    if (id != null) {
                        productRef.child(id).setValue(product)
                    }
                }
            }
        }

        binding.btUpdate.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                if(user != null){
                    val map = HashMap<String, Product>()
                    val id = binding.etIdToModify.text.toString()
                    val name = binding.etProductName.text.toString()
                    val price = binding.etPrice.text.toString().toDouble()
                    val number = binding.etNumber.text.toString().toInt()
                    val isBought = binding.checkBox.isChecked
                    val product = Product(id, name, price, number, isBought)
                    map.put(binding.etIdToModify.text.toString() , product)
                    productRef.updateChildren(map as Map<String, Any>)
                }
            }
        }
    }
}