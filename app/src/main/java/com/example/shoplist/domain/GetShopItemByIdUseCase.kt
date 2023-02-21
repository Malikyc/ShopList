package com.example.shoplist.domain

class GetShopItemByIdUseCase(private val shopListRepository: ShopListRepository) {
    fun getShopItemById(id:Int) : ShopItem{
        return shopListRepository.getShopItemById(id)
    }
}