package com.example.shoplist.data

import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {
    private var shopList = mutableListOf<ShopItem>()
    private var autoIncrementId = 0

    override fun addShopItem(shopItem: ShopItem) {
        shopItem.id = autoIncrementId++
        shopList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem): ShopItem {
        val oldElement = getShopItemById(shopItem.id)
        deleteShopItem(oldElement)
        if (shopItem.id == ShopItem.UNDEFINDED_ID){
            shopItem.id = autoIncrementId++
        }
        return shopItem
    }

    override fun getShopItemById(id: Int): ShopItem {
        return shopList.find { it.id == id }?: throw RuntimeException("Element with such id wasnt found")
    }

    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }
}