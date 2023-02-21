package com.example.shoplist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R

class ShopItemViewHolder(view : View) : RecyclerView.ViewHolder(view){
    val name = view.findViewById<TextView>(R.id.tv_name)
    val amount = view.findViewById<TextView>(R.id.tv_amount)
}