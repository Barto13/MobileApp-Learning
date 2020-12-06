package com.barto.simplecrud

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDAO {

    @Query("SELECT * FROM product")
    fun getProducts(): LiveData<List<Product>>

    @Insert
    fun insert(product: Product)

    @Delete
    fun delete(product: Product)

    @Update
    fun update(product: Product)

    @Query("DELETE FROM product")
    fun deleteAll()
}