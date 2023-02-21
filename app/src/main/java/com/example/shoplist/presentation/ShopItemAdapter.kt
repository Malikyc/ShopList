package com.example.shoplist.presentation
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoplist.R
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
        if (viewType == activeType){
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_active_shop,parent,false)
        return ShopItemViewHolder(view)}
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inaactive_shop,parent,false)
            return ShopItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
         val shopItem = getItem(position)
        with(holder) {
            name.text = shopItem.name
            amount.text = shopItem.amount.toString()
            itemView.setOnLongClickListener {
                onLongItemClickListener?.invoke(shopItem)
                true
            }
            itemView.setOnClickListener{
                onItemClickListener?.invoke(shopItem)
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