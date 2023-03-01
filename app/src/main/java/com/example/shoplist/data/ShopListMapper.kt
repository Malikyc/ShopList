package com.example.shoplist.data

import com.example.shoplist.domain.ShopItem

class ShopListMapper {

    fun mapDbModelToEntrity(shopItemDbModel: ShopItemDbModel): ShopItem {
        return ShopItem(
            shopItemDbModel.name,
            shopItemDbModel.amount, shopItemDbModel.isActive, shopItemDbModel.id
        )
    }

    fun mapEntritytoDbModel(shopItem: ShopItem): ShopItemDbModel {
        return ShopItemDbModel(
            shopItem.id,
            shopItem.name,
            shopItem.amount, shopItem.isActive
        )
    }

    fun mapListOfDbModelToListOfEntities(listM:List<ShopItemDbModel>):List<ShopItem>{
        return listM.map { mapDbModelToEntrity(it) }
    }
}