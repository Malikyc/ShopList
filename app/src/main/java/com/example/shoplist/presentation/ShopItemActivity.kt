package com.example.shoplist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.shoplist.databinding.ActivityShopItemBinding
import com.example.shoplist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShopItemBinding
    private lateinit var tilNameAct : TextInputLayout
    private lateinit var tilAmountAct : TextInputLayout
    private lateinit var etNameAct :  EditText
    private lateinit var etAmountAct : EditText
    private lateinit var saveButtonAct : Button

    private lateinit var viewModel: ShopItemViewModel

    private var current_mode = UNIDENTIFED_MODE
    private var shopItem_id = ShopItem.UNDEFINDED_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this@ShopItemActivity)[ShopItemViewModel::class.java]
        parseIntent()
        initViews()
        if(current_mode == EXTRA_MODE_ADD){
            subscribeOnLiveData()
            createOnWriteListener()
            addNewItem()
        }
        else if (current_mode == EXTRA_MODE_EDIT){
            subscribeOnLiveData()
            createOnWriteListener()
            editItem()
        }



    }

    private fun parseIntent(){
        val mode = intent.getStringExtra(EXTRA_MODE)
        if(mode != EXTRA_MODE_ADD && mode != EXTRA_MODE_EDIT){
            throw RuntimeException("Mode is not found")
        }
        if(mode == EXTRA_MODE_EDIT){
            if(!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("ShopItem id is not found")
            }
            shopItem_id = intent.getIntExtra(EXTRA_SHOP_ITEM_ID,ShopItem.UNDEFINDED_ID)
            current_mode = EXTRA_MODE_EDIT
        }
        if (mode == EXTRA_MODE_ADD){
            current_mode = EXTRA_MODE_ADD
        }
    }

    private fun initViews(){
        with(binding){
            tilNameAct = tilName
            tilAmountAct = tilAmount
            etNameAct = etName
            etAmountAct = etAmount
            saveButtonAct = saveButton
        }
    }

    private fun addNewItem(){
        saveButtonAct.setOnClickListener{
            val name :String = etNameAct.text.toString()
            val amount = etAmountAct.text.toString()
            viewModel.addNewShopItem(name,amount)
        }

    }
    private fun editItem(){
        viewModel.getShopItem(shopItem_id)
        viewModel.shopItemLive.observe(this) {
            etNameAct.setText(it.name)
            etAmountAct.setText(it.amount.toString())
        }
        saveButtonAct.setOnClickListener{
            val name :String = etNameAct.text.toString()
            val amount = etAmountAct.text.toString()
            viewModel.editShopItem(name,amount)
        }

    }

    fun subscribeOnLiveData(){
        viewModel.errorInputName.observe(this) {
            when(it){
                true-> tilNameAct.error = "Error"
                false-> tilNameAct.error = null
            }
        }
        viewModel.errorInputAmount.observe(this) {
            when(it){
                true-> tilAmountAct.error = "Error"
                false-> tilAmountAct.error = null
            }
        }
        viewModel.shouldBeClosed.observe(this) {
            if(true){
                intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
        }
        }
    }

    private fun createOnWriteListener(){

     etNameAct.addTextChangedListener(object : TextWatcher{
         override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
         }

         override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             viewModel.resetErrorInputName()
         }

         override fun afterTextChanged(s: Editable?) {
         }

     })
     etAmountAct.addTextChangedListener(object : TextWatcher{
         override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
         }

         override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             viewModel.resetErrorInputAmount()
         }

         override fun afterTextChanged(s: Editable?) {
         }

     })
    }

    companion object{
        const val EXTRA_MODE = "extra_mode"
        const val EXTRA_MODE_ADD = "mode_add"
        const val EXTRA_MODE_EDIT = "mode_edit"
        const val EXTRA_SHOP_ITEM_ID = "shop_item_id"
        const val UNIDENTIFED_MODE = ""

        fun newIntentAdd(context : Context) : Intent{
            val intent = Intent(context,ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, EXTRA_MODE_ADD)
            return intent
        }

        fun newIntentEdit(context : Context,shopItemId: Int) : Intent{
            val intent = Intent(context,ShopItemActivity::class.java)
            intent.putExtra(EXTRA_MODE, EXTRA_MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID,shopItemId)
            return intent
        }

    }
}