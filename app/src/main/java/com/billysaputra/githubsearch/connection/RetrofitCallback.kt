package com.billysaputra.githubsearch.connection

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitCallback<T> : Callback<T> {
    var onResponse: ((Call<T>, Response<T>) -> Unit)? = null
    var onFailure: ((Call<T>, t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(call, t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(call, response)
    }
}