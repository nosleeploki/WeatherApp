package com.example.login.presentation.ui.binding

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@BindingAdapter("android:text")
fun bindEditText(editText: EditText, text: LiveData<String>?){
    editText.setText(text?.value ?: "")
}

@BindingAdapter("android:text")
fun bindEditTextLiveData(editText: EditText, text: MutableLiveData<String>?) {
    editText.setText(text?.value ?: "")
}

@BindingAdapter("android:text")
fun setPhoneNumber(editText: EditText, value: Int?){
    val newValue = value?.toString() ?: ""
    if (editText.text.toString() != newValue){
        editText.setText(newValue)
    }
}

@BindingAdapter("android:textAttrChanged")
fun setPhoneNumberListener(editText: EditText,listener:InverseBindingListener?){
    if (listener != null){
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                listener.onChange()
            }
        })
    }
}

@InverseBindingAdapter(attribute = "android:text")
fun getPhoneNumber(editText: EditText): Int? {
    val text = editText.text.toString()
    return text.toIntOrNull()
}