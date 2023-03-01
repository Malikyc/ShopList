package com.example.shoplist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoplist.domain.ShopItem

@Entity(tableName = "shop_items")
data class ShopItemDbModel (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var name : String,
    var amount : Int,
    var isActive: Boolean
        )