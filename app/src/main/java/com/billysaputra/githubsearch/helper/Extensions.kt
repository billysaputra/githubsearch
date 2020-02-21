package com.billysaputra.githubsearch.helper

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.billysaputra.githubsearch.connection.RetrofitCallback
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call

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

/**
 * Retrofit Call
 * */
internal fun <T> Call<T>.enqueue(callback: RetrofitCallback<T>.() -> Unit) {
    val callBackKt = RetrofitCallback<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

/**
 * View
 * */
fun View.showSnackbar(snackbarMessage: String, timeLength: Int) {
    Snackbar.make(this, snackbarMessage, timeLength).show()
}