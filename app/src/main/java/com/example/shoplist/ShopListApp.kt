package com.example.shoplist

import android.app.Application
import com.example.shoplist.di.DaggerShopListComponent

class ShopListApp : Application() {
   val component by lazy {
  DaggerShopListComponent.factory().create(this)
   }
}