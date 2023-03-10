package com.example.shoplist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val mapOfViewModelProviders : @JvmSuppressWildcards Map<Class<out ViewModel>,Provider<ViewModel>>
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return mapOfViewModelProviders[modelClass]?.get() as T
    }
}