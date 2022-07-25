package ru.netology.nmedia.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

internal fun View.hideKeyboard() {
    val hideKb = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    hideKb.hideSoftInputFromWindow(windowToken,/*flags*/0)
}