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


class ProductAdapter(val context: Context, val list: ArrayList<Product>, val ref: DatabaseReference) :
        RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    init{
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                CoroutineScope(IO).launch {
                    val product = snapshot.getValue(Product::class.java)
                    if (product != null) {
                        list.add(product)
                    }
                    withContext(Main){
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                CoroutineScope(IO).launch {
                    val key = snapshot.key
                    val product = snapshot.getValue(Product::class.java)
                    list.removeIf{it.id == key}
                    if (product != null) {
                        list.add(product)
                    }
                    withContext(Main){
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                CoroutineScope(IO).launch{
                    val product = snapshot.getValue(Product::class.java)
                    list.remove(product)
                    withContext(Main){
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

    class MyViewHolder(val binding: ElementListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ElementListBinding.inflate(inflater)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvId.text = list[position].id.toString()
        holder.binding.tvName.text = list[position].name
        holder.binding.tvPrice.text = list[position].price.toString()
        holder.binding.tvNumber.text = list[position].number.toString()
        holder.binding.checkIsBought.isChecked = list[position].isBought

        holder.binding.root.setOnClickListener{
            val chosenId = list[position].id.toString()
            val chosenName = list[position].name
            val chosenPrice = list[position].price.toString()
            val chosenNumber = list[position].number.toString()
            val chosenIsBought = list[position].isBought.toString()

            if(ref.toString() == "https://myandroidapp-4f0ac-default-rtdb.firebaseio.com/public/products")
            {
                val intent1 = Intent(context, PublicListActivity::class.java)
                intent1.putExtra("chosenId", chosenId)
                intent1.putExtra("chosenName", chosenName)
                intent1.putExtra("chosenPrice", chosenPrice)
                intent1.putExtra("chosenNumber", chosenNumber)
                intent1.putExtra("chosenIsBought", chosenIsBought)
                Toast.makeText(context,"chosen is bought:  "  + chosenIsBought, Toast.LENGTH_SHORT).show()
                context.startActivity(intent1)
            }else{
                val intent1 = Intent(context, ListActivity::class.java)
                intent1.putExtra("chosenId", chosenId)
                intent1.putExtra("chosenName", chosenName)
                intent1.putExtra("chosenPrice", chosenPrice)
                intent1.putExtra("chosenNumber", chosenNumber)
                intent1.putExtra("chosenIsBought", chosenIsBought)
                Toast.makeText(context,"chosen is bought:  "  + chosenIsBought, Toast.LENGTH_SHORT).show()
                context.startActivity(intent1)
            }
        }
        holder.binding.root.setOnLongClickListener{
            ref.child(list[position].id!!).removeValue()
            Toast.makeText(context, list[position].id + " Was Deleted!", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun getItemCount(): Int = list.size

}

