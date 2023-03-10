package com.example.shoplist.di

import android.app.Application
import com.example.shoplist.presentation.MainActivity
import com.example.shoplist.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class,ViewModelModule::class])
interface ShopListComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: ShopItemFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application) : ShopListComponent
    }
}