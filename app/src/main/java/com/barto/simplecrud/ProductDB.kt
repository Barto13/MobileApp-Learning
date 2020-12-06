package com.barto.simplecrud

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class ProductDB : RoomDatabase() {

    abstract fun productDAO(): ProductDAO

    companion object{
        private var instance: ProductDB? = null

        fun getDatabase(context: Context): ProductDB{
            if(instance != null)
                return instance as ProductDB
            instance = Room.databaseBuilder(
                    context,
                    ProductDB::class.java,
                    "productDB"
            ).allowMainThreadQueries().build()
            return instance as ProductDB
        }
    }
}