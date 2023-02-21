package com.example.shoplist.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val liveShopList = MutableLiveData<List<ShopItem>>()
    private var shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })

    private var autoIncrementId = 0
    init {
        for(i in 0 until 10){
            val item = ShopItem("N$i",1, Random.nextBoolean())
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == ShopItem.UNDEFINDED_ID){
        shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        udpateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        udpateList()
    }

    override fun editShopItem (shopItem: ShopItem) {
        val oldElement = getShopItemById(shopItem.id)
        deleteShopItem(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItemById(shopItemId: Int): ShopItem {
        return shopList.find { it.id == shopItemId }?: throw RuntimeException("Element with such id wasnt found")
    }

    override fun getShopList(): MutableLiveData<List<ShopItem>>{
        return liveShopList
    }
    private fun udpateList(){
        liveShopList.value = shopList.toList()
    }
}