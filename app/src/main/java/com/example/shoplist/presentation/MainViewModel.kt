package com.example.shoplist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.data.ShopListRepositoryImpl
import com.example.shoplist.domain.*

class MainViewModel : ViewModel() {
    private val shopListRepository = ShopListRepositoryImpl


    private val deleteShopItemUseCase = DeleteShopItemUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)

     val listOfItems = shopListRepository.getShopList()




    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)

    }

    fun changeIsActiveState(shopItem: ShopItem){
        val newItem = shopItem.copy(isActive = !shopItem.isActive)
        editShopItemUseCase.editShopItem(newItem)
    }

}