package com.example.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoplist.R
import com.example.shoplist.databinding.FragmentShopItemBinding
import com.example.shoplist.databinding.ItemActiveShopBinding
import com.example.shoplist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment() : Fragment() {

    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }

    private lateinit var onEditingFinishedListener : OnEditingFinishedListener

    private var _binding : FragmentShopItemBinding? = null
    private val binding : FragmentShopItemBinding
        get() {
            return _binding ?: throw RuntimeException("FragmentShopItemBinding is null")
        }

    private var current_mode : String = UNIDENTIFED_MODE
    private var shopItem_id : Int = ShopItem.UNDEFINDED_ID


    private val viewModel: ShopItemViewModel by lazy {
        ViewModelProvider(this)[ShopItemViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("SequenceOfLifeCycle","onAttach")
        if(context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        }
        else{
            throw RuntimeException("activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("SequenceOfLifeCycle","onCreate")
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("SequenceOfLifeCycle","onCreateView")
        _binding = FragmentShopItemBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("SequenceOfLifeCycle","onViewCreated")

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        subscribeOnLiveData()
        turnOnRightMode()
    }


    private fun parseParams(){
        var args = requireArguments()
        if(!args.containsKey(EXTRA_MODE)){
            throw RuntimeException("Mode is not found")
        }
        val mode = args.getString(EXTRA_MODE)
        if(mode != EXTRA_MODE_ADD && mode != EXTRA_MODE_EDIT){
                throw RuntimeException("Unlnown mode")
        }
         current_mode = mode
        if (mode == EXTRA_MODE_EDIT && !args.containsKey(EXTRA_SHOP_ITEM_ID)){
            throw RuntimeException("ShopItem id not found")
        }
        shopItem_id = args.getInt(EXTRA_SHOP_ITEM_ID)

    }


    private fun addNewItem(){
        binding.saveButton.setOnClickListener{
            val name :String = binding.etName.text.toString()
            val amount = binding.etAmount.text.toString()
            viewModel.addNewShopItem(name,amount)
        }

    }
    private fun editItem(){
        viewModel.getShopItem(shopItem_id)
        binding.saveButton.setOnClickListener{
            val name :String = binding.etName.text.toString()
            val amount = binding.etAmount.text.toString()
            viewModel.editShopItem(name,amount)
        }

    }
//
    fun subscribeOnLiveData(){
//
        viewModel.shouldBeClosed.observe(viewLifecycleOwner) {
            if(true){
                onEditingFinishedListener.onEditingFinished()
        }
        }
    }
    private fun turnOnRightMode(){
        if(current_mode == EXTRA_MODE_ADD){
            addNewItem()
        }
        else if (current_mode == EXTRA_MODE_EDIT){
            editItem()
        }
    }

    companion object {
        const val EXTRA_MODE = "extra_mode"
        const val EXTRA_MODE_ADD = "mode_add"
        const val EXTRA_MODE_EDIT = "mode_edit"
        const val EXTRA_SHOP_ITEM_ID = "shop_item_id"
        const val UNIDENTIFED_MODE = ""

        fun newAddFragment(): ShopItemFragment {
            return ShopItemFragment().apply {
               arguments = Bundle().apply {
                    putString(EXTRA_MODE, EXTRA_MODE_ADD)
                }
            }
        }

        fun newEditFragment(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
               arguments = Bundle().apply {
                    putString(EXTRA_MODE, EXTRA_MODE_EDIT)
                    putInt(EXTRA_SHOP_ITEM_ID, shopItemId)
                }
            }
        }

    }
}