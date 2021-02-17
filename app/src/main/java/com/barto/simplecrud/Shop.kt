package com.barto.simplecrud

data class Shop (var name: String = "",
                 var desc: String = "",
                 var latitude: Double = 0.0,
                 var longitude: Double = 0.0,
                 var radius: Float = 100f,
                )