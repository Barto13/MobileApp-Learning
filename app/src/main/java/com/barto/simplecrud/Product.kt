package com.barto.simplecrud

import androidx.room.Entity
import androidx.room.PrimaryKey


//@Entity
//data class Product (@PrimaryKey(autoGenerate = true) var id: Long = 0, var name: String, var price: Double, var number: Int, var isBought: Boolean)
data class Product(var id: String? = "",
                   var name: String = "",
                   var price: Double = 0.0,
                   var number: Int = 0,
                   var isBought: Boolean = false
                   )

