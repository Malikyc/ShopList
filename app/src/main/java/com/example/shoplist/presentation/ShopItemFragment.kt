package com.example.shoplist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoplist.R
import com.example.shoplist.databinding.ActivityShopItemBinding
import com.example.shoplist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment() : Fragment() {

    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }

    private lateinit var onEditingFinishedListener : OnEditingFinishedListener

    private var current_mode : String = UNIDENTIFED_MODE
    private var shopItem_id : Int = ShopItem.UNDEFINDED_ID

        private lateinit var tilNameAct : TextInputLayout
    private lateinit var tilAmountAct : TextInputLayout
    private lateinit var etNameAct : EditText
    private lateinit var etAmountAct : EditText
    private lateinit var saveButtonAct : Button

    private lateinit var viewModel: ShopItemViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        }
        else{
            throw RuntimeException("activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        subscribeOnLiveData()
        createOnWriteListener()
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

    private fun initViews(view: View){
            tilNameAct = view.findViewById(R.id.tilName)
            tilAmountAct = view.findViewById(R.id.tilAmount)
            etNameAct = view.findViewById(R.id.etName)
            etAmountAct = view.findViewById(R.id.etAmount)
            saveButtonAct = view.findViewById(R.id.saveButton)

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
        viewModel.shopItemLive.observe(viewLifecycleOwner) {
            etNameAct.setText(it.name)
            etAmountAct.setText(it.amount.toString())
        }
        saveButtonAct.setOnClickListener{
            val name :String = etNameAct.text.toString()
            val amount = etAmountAct.text.toString()
            viewModel.editShopItem(name,amount)
        }

    }
//
    fun subscribeOnLiveData(){
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            when(it){
                true-> tilNameAct.error = "Error"
                false-> tilNameAct.error = null
            }
        }
        viewModel.errorInputAmount.observe(viewLifecycleOwner) {
            when(it){
                true-> tilAmountAct.error = "Error"
                false-> tilAmountAct.error = null
            }
        }
        viewModel.shouldBeClosed.observe(viewLifecycleOwner) {
            if(true){
                onEditingFinishedListener.onEditingFinished()
        }
        }
    }
//
    private fun createOnWriteListener(){

     etNameAct.addTextChangedListener(object : TextWatcher {
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