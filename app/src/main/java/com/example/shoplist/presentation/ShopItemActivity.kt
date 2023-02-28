package com.example.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoplist.R
import com.example.shoplist.domain.ShopItem

class ShopItemActivity : AppCompatActivity(),ShopItemFragment.OnEditingFinishedListener {
    private var current_mode = UNIDENTIFED_MODE
    private var shopItem_id = ShopItem.UNDEFINDED_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        if(savedInstanceState == null){
        turnOnRightMode()
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

    private fun turnOnRightMode(){
    val fragment =when(current_mode ){
        EXTRA_MODE_ADD -> ShopItemFragment.newAddFragment()
        EXTRA_MODE_EDIT -> ShopItemFragment.newEditFragment(shopItem_id)
        else -> throw RuntimeException("Unknown screenMode")
        }
    supportFragmentManager.beginTransaction()
        .replace(R.id.shop_item_container, fragment)
        .commit()
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

    override fun onEditingFinished() {
        finish()
    }
}