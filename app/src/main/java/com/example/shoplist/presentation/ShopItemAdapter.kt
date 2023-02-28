package com.example.shoplist.presentation
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.example.shoplist.R
import com.example.shoplist.databinding.ItemActiveShopBinding
import com.example.shoplist.databinding.ItemInaactiveShopBinding
import com.example.shoplist.domain.ShopItem

class ShopItemAdapter : ListAdapter<ShopItem,ShopItemViewHolder>(SimpleItemCallBack()){
    companion object{
        const val activeType = 1
        const val inActivetype = 2
        const val maxPool = 5
    }
    var onLongItemClickListener : ((ShopItem)->Unit)? = null
    var onItemClickListener : ((ShopItem)->Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType){
        activeType->R.layout.item_active_shop
        inActivetype->R.layout.item_inaactive_shop
        else -> throw RuntimeException("Unknown viewType")}
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layout,
                parent,
                false
            )
            return ShopItemViewHolder(binding)
        }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
          val shopItem = getItem(position)
           val binding = holder.binding
            binding.root.setOnLongClickListener {
                onLongItemClickListener?.invoke(shopItem)
                true
            }
            binding.root.setOnClickListener{
                onItemClickListener?.invoke(shopItem)
        }
        when(binding){
        is ItemActiveShopBinding ->{
            binding.tvName.text = shopItem.name
            binding.tvAmount.text = shopItem.amount.toString()
        }
        is ItemInaactiveShopBinding->{
            binding.tvName.text = shopItem.name
            binding.tvAmount.text = shopItem.amount.toString()
        }
        }

    }


    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)
        if (shopItem.isActive){
            return activeType
        }
        else{
            return inActivetype
        }
    }
}