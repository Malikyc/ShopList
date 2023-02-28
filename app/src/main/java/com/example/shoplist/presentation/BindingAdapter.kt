package com.example.shoplist.presentation

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

interface onWriteChange{
    fun reactOnChange()
}

@BindingAdapter("isInErrorInput")
fun bindIsInErrorInput(textInputLayout: TextInputLayout,boolean: Boolean){
    when(boolean){
        true-> textInputLayout.error = "Error"
        false-> textInputLayout.error = null
    }
}
 @BindingAdapter("onWriteListener")
fun bindOnWriteListener(textInputEditText: TextInputEditText,onWriteChange: onWriteChange ){
    textInputEditText.addTextChangedListener(object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onWriteChange.reactOnChange()
        }

        override fun afterTextChanged(s: Editable?) {
        }

    })
}
