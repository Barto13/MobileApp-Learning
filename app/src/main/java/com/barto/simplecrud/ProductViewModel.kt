package com.barto.simplecrud

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: ProductRepo
    val allProducts: LiveData<List<Product>>

    init{
        repo = ProductRepo(ProductDB.getDatabase(application.applicationContext).productDAO())
        allProducts = repo.allProducts
    }

    fun insert(product: Product) = repo.insert(product)

    fun delete(product: Product) = repo.delete(product)

    fun update(product: Product) = repo.update(product)

    fun deleteAll() = repo.deleteAll()

}