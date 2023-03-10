package com.example.shoplist.di

import android.app.Application
import com.example.shoplist.data.AppDatabase
import com.example.shoplist.data.AppDatabaseDao
import com.example.shoplist.data.ShopListRepositoryImpl
import com.example.shoplist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl) : ShopListRepository

    companion object{
        @ApplicationScope
        @Provides
        fun provideDataBaseDao(application: Application):AppDatabaseDao{
            return AppDatabase.getInstance(application).appDataBaseDao()
        }
    }
}