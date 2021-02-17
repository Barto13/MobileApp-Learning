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
import com.barto.simplecrud.databinding.ActivityShoplistBinding
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

class ShopListActivity : AppCompatActivity(){

    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityShoplistBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val intent1 = intent
        binding.rv1.layoutManager = LinearLayoutManager(this)
        binding.rv1.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //firebase
        val user = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()
        val refUser = database.getReference("public/")
        val productRef = refUser.child("shops")
        val list = arrayListOf<Shop>()
        val adapter = ShopAdapter(this, list, productRef)
        binding.rv1.adapter = adapter

        binding.btNextList.setOnClickListener{
            //nothing
        }

        binding.button.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                if(user != null){
                    val name = binding.etSName.text.toString().trim()
                    val desc = binding.etSDesc.text.toString().trim()
                    val latitude = binding.etLatitude.text.toString().trim().toDouble()
                    val longitude = binding.etLongitude.text.toString().trim().toDouble()
                    val radius = binding.etRadius.text.toString().trim().toFloat()
                    val shop = Shop(name, desc, latitude, longitude, radius)
                    if (name != null) {
                        productRef.child(name).setValue(shop)
                    }
                }
            }
        }
        binding.btUpdate.setOnClickListener{
            //nothing
        }
    }
}