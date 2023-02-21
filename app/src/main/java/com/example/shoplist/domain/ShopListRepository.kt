package com.example.shoplist.domain

import androidx.lifecycle.MutableLiveData

interface ShopListRepository {
    fun addShopItem(shopItem: ShopItem)
    fun deleteShopItem(shopItem: ShopItem)
    fun editShopItem(shopItem: ShopItem)
    fun getShopItemById(id:Int):ShopItem
    fun getShopList() : MutableLiveData<List<ShopItem>>
}