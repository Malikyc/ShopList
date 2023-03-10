package com.example.shoplist.presentation

import android.app.FragmentContainer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplist.R
import com.example.shoplist.ShopListApp
import com.example.shoplist.data.AppDatabase
import com.example.shoplist.databinding.ActivityMainBinding
import com.example.shoplist.domain.ShopItem
import javax.inject.Inject


class MainActivity : AppCompatActivity(),ShopItemFragment.OnEditingFinishedListener {
    private val component by lazy {
        (application as ShopListApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by lazy{
        ViewModelProvider(this,viewModelFactory)[MainViewModel::class.java]
    }
    private lateinit var shopItemAdapter: ShopItemAdapter
    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRv()
        viewModel.listOfItems.observe(this, {
            shopItemAdapter.submitList(it)
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
        setButtonOnClikcListener()
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
        if (!isLandOrient()){
        shopItemAdapter.onItemClickListener = {
            val intent = ShopItemActivity.newIntentEdit(this@MainActivity,it.id)
            startActivity(intent)}
        }
        else{
            shopItemAdapter.onItemClickListener =  {
                val fragment = ShopItemFragment.newEditFragment(it.id)
                lauchFragment(fragment)
            } }
    }
    private fun setButtonOnClikcListener() {
        if (!isLandOrient()) {
            binding.floatingActionButton.setOnClickListener({
                val intent = ShopItemActivity.newIntentAdd(this)
                startActivity(intent)
            })
        }
        else{
            binding.floatingActionButton.setOnClickListener({
                val fragment = ShopItemFragment.newAddFragment()
                lauchFragment(fragment)
            })

        }

    }

    private fun lauchFragment(fragment: ShopItemFragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view_tag, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setUpLongClickListener() {
        shopItemAdapter.onLongItemClickListener = {
            viewModel.changeIsActiveState(it)
        }
    }
    private fun isLandOrient():Boolean{
       return when(binding.fragmentContainerViewTag){
            null ->false
            else ->true
        }
    }

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
    }
}


