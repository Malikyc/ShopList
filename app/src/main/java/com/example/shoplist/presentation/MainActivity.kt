package com.example.shoplist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.databinding.ActivityMainBinding
import com.example.shoplist.domain.ShopItem

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopItemAdapter: ShopItemAdapter
    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initRv()
        viewModel.listOfItems.observe(this, {
            shopItemAdapter.submitList(it)
        })
        binding.floatingActionButton.setOnClickListener({
            val intent = ShopItemActivity.newIntentAdd(this)
            startActivity(intent)
        })


    }

    fun initRv(){

        shopItemAdapter = ShopItemAdapter()
        with(binding){
            with(recyclerView){
                adapter = shopItemAdapter
                recycledViewPool.setMaxRecycledViews(ShopItemAdapter.activeType,ShopItemAdapter.maxPool)
                recycledViewPool.setMaxRecycledViews(ShopItemAdapter.inActivetype,ShopItemAdapter.maxPool)
            }
        }

        setUpLongClickListener()
        setUpOnClickListener()
        setUpOnSwipeListener()

    }

    private fun setUpOnSwipeListener() {
        val simpleCallback =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(1, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    var shopItem = shopItemAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteShopItem(shopItem)
                }

            })
        simpleCallback.attachToRecyclerView(binding.recyclerView)
    }

    private fun setUpOnClickListener() {
        shopItemAdapter.onItemClickListener = {
            val intent = ShopItemActivity.newIntentEdit(this@MainActivity,it.id)
            startActivity(intent)
        }
    }

    private fun setUpLongClickListener() {
        shopItemAdapter.onLongItemClickListener = {
            viewModel.changeIsActiveState(it)
        }
    }
}


