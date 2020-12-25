package com.barto.simplecrud

class ProductRepo(private val productDAO: ProductDAO) {

    val allProducts = productDAO.getProducts()

    fun insert(product: Product) = productDAO.insert(product)

    fun delete(product: Product) = productDAO.delete(product)

    fun update(product: Product) = productDAO.update(product)

    fun deleteAll() = productDAO.deleteAll()

}