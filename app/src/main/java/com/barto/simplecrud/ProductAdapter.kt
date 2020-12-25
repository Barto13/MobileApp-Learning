package com.barto.simplecrud

import android.content.Context
import android.content.SharedPreferences
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.barto.simplecrud.databinding.ElementListBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_options.*


class ProductAdapter(val viewModel: ProductViewModel) : RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    var products = emptyList<Product>()
    var fontSize = 8
    private lateinit var sp2: SharedPreferences

    class MyViewHolder(val binding: ElementListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ElementListBinding.inflate(inflater)
        return MyViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvId.text = products[position].id.toString()
        holder.binding.tvName.text = products[position].name
        holder.binding.tvPrice.text = products[position].price.toString()
        holder.binding.tvNumber.text = products[position].number.toString()
        holder.binding.checkIsBought.isChecked = products[position].isBought

        sp2 = holder.binding.root.context.getSharedPreferences("Font", Context.MODE_PRIVATE)

        val font1 = sp2.getString("fontSize1", "default")

        when(font1){
            "FontSizeSmall" -> fontSize = 20
            "FontSizeLarge" -> fontSize = 28
            "FontSizeDefault" -> fontSize = 14
        }

        holder.binding.tvId.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        holder.binding.tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        holder.binding.tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        holder.binding.tvNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
        holder.binding.checkIsBought.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())


        holder.binding.root.setOnClickListener{
            viewModel.delete(products[position])
            notifyDataSetChanged()
        }
        holder.binding.checkIsBought.setOnClickListener{
            products[position].isBought = (it as CheckBox).isChecked
            viewModel.update(products[position])
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int = products.size

    fun setProductList(productList: List<Product>){
        products = productList
        notifyDataSetChanged()
    }
}

