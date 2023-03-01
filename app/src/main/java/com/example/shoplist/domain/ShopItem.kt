package com.example.shoplist.domain

data class ShopItem(
    var name : String,
    var amount : Int,
    var isActive: Boolean,
    var id : Int = UNDEFINDED_ID
)
{companion object{
    const val UNDEFINDED_ID = 0
}}
