package com.barto.simplecrud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.sax.Element
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.barto.simplecrud.databinding.ElementListBinding
import com.barto.simplecrud.databinding.ElementListshopsBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_options.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class ShopAdapter(val context: Context, val list: ArrayList<Shop>, val ref: DatabaseReference) :
    RecyclerView.Adapter<ShopAdapter.MyViewHolder>() {

    init {
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                CoroutineScope(IO).launch {
                    val shop = snapshot.getValue(Shop::class.java)
                    if (shop != null) {
                        list.add(shop)
                    }
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                CoroutineScope(IO).launch {
                    val key = snapshot.key
                    val shop = snapshot.getValue(Shop::class.java)
                    list.removeIf { it.name == key }
                    if (shop != null) {
                        list.add(shop)
                    }
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                CoroutineScope(IO).launch {
                    val shop = snapshot.getValue(Shop::class.java)
                    list.remove(shop)
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("read-error", error.details)
            }
        })
    }


    class MyViewHolder(val binding: ElementListshopsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ElementListshopsBinding.inflate(inflater)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvSName.text = list[position].name.toString()
        holder.binding.tvSDesc.text = list[position].desc
        holder.binding.tvLatitude.text = list[position].latitude.toString()
        holder.binding.tvLongitude.text = list[position].longitude.toString()
        holder.binding.tvRadius.text = list[position].radius.toString()
        holder.binding.root.setOnClickListener {
            //nothing
        }
        holder.binding.root.setOnLongClickListener {
            ref.child(list[position].name!!).removeValue()
            Toast.makeText(context, list[position].name + " Was Deleted!", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun getItemCount(): Int = list.size

}