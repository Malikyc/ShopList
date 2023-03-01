package com.example.shoplist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AppDatabaseDao {
    @Query("SELECT * FROM shop_items")
     fun getShopItemList() : LiveData<List<ShopItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItemDbModel: ShopItemDbModel)

    @Query("DELETE FROM shop_items WHERE id=:shopItemDbModelId")
    suspend fun deleteShopItem(shopItemDbModelId: Int)

    @Query("SELECT * FROM shop_items WHERE id=:shopItemId LIMIT 1")
    suspend fun getShopItem(shopItemId : Int) : ShopItemDbModel
}