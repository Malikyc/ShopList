package com.example.shoplist.presentation

import android.app.Application
import androidx.lifecycle.*
import com.example.shoplist.data.ShopListRepositoryImpl
import com.example.shoplist.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val addShopItemUseCase : AddShopItemUseCase,
            private val editShopItemUseCase : EditShopItemUseCase,
            private val getShopItemByIdUseCase : GetShopItemByIdUseCase
        ) : ViewModel() {


    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName : LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputAmount = MutableLiveData<Boolean>()
    val errorInputAmount: LiveData<Boolean>
        get() = _errorInputAmount

    private val _shopItemLive = MutableLiveData<ShopItem>()
    val shopItemLive : LiveData<ShopItem>
        get() = _shopItemLive

    private val _shouldBeClosed = MutableLiveData<Boolean>()
       val shouldBeClosed : LiveData<Boolean>
       get() = _shouldBeClosed


    fun addNewShopItem (inputName : String? , inputAmount : String?) {
         val name = parseName(inputName)
            val amount = parseAmount(inputAmount)
            if(validateInput(name,amount)) {
                viewModelScope.launch {
                    val shopItem = ShopItem(name, amount, true)
                    addShopItemUseCase.addShopItem(shopItem)
                    finishWork()
                }
            }

    }

    fun editShopItem(inputName : String? , inputAmount : String?){
         val name = parseName(inputName)
            val amount = parseAmount(inputAmount)
            if(validateInput(name,amount)){
                viewModelScope.launch {
                _shopItemLive.value?.let {
                    val shopItem = it.copy(name = name, amount = amount)
                    editShopItemUseCase.editShopItem(shopItem)
                    finishWork()
                }
            } }

    }

    fun getShopItem(id:Int){
        viewModelScope.launch {
            val shopItem = getShopItemByIdUseCase.getShopItemById(id)
            _shopItemLive.value = shopItem }

    }


    private fun parseName(inputName: String?) :String{
     return inputName?.trim() ?: ""
 }

    private fun parseAmount(inputAmount: String?) : Int{
        return try {
            inputAmount?.trim()?.toInt() ?: 0
        }
        catch (e:Exception){
             0
        }

    }
    private fun validateInput(name:String,amount:Int) : Boolean{
      var result = true
      if(name.isBlank()){
          _errorInputName.value = true
          result = false
      }
        if (amount <= 0){
            _errorInputAmount.value = true
            result = false
        }
        return result
 }

    fun resetErrorInputName(){
        _errorInputName.value = false
    }

    fun resetErrorInputAmount(){
        _errorInputAmount.value = false
    }
    private fun finishWork(){
        _shouldBeClosed.value = true
    }

}