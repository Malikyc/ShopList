package com.example.shoplist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.shoplist.domain.ShopItem
import com.example.shoplist.domain.ShopListRepository

class ShopListRepositoryImpl(application: Application) :
    ShopListRepository {

    private val dbDao = AppDatabase.getInstance(application).appDataBaseDao()
    private val mapper = ShopListMapper()

    override suspend fun addShopItem(shopItem: ShopItem) {
        dbDao.addShopItem(mapper.mapEntritytoDbModel(shopItem))
    }

    override suspend  fun deleteShopItem(shopItem: ShopItem) {
        dbDao.deleteShopItem(mapper.mapEntritytoDbModel(shopItem).id)
    }

    override suspend fun editShopItem (shopItem: ShopItem) {
        addShopItem(shopItem)
    }

    override suspend fun getShopItemById(shopItemId: Int): ShopItem {
        val shopItemDbModel = dbDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntrity(shopItemDbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        dbDao.getShopItemList()
    ){
        mapper.mapListOfDbModelToListOfEntities(it)
    }

}