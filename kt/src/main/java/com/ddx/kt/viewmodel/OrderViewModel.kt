package com.ddx.kt.viewmodel


data class FoodInfo(
    val name: String,
    val price: String,
    val count: Int
)

data class OrderInfo(
    val foods: MutableList<FoodInfo>
)

class OrderViewModel {
}