package com.example.shoplist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplist.data.ShopListRepositoryImpl
import com.example.shoplist.domain.DeleteShopItemUseCase
import com.example.shoplist.domain.EditShopItemUseCase
import com.example.shoplist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val shopListRepository = ShopListRepositoryImpl(application)


    private val deleteShopItemUseCase = DeleteShopItemUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)


     val listOfItems = shopListRepository.getShopList()




    fun deleteShopItem(shopItem: ShopItem){
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)  }


    }

    fun changeIsActiveState(shopItem: ShopItem){
        viewModelScope.launch {
            val newItem = shopItem.copy(isActive = !shopItem.isActive)
            editShopItemUseCase.editShopItem(newItem) }

    }

}