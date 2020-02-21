package com.billysaputra.githubsearch.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Activity
 * */
internal fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/**
 * Context
 * */
internal fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}