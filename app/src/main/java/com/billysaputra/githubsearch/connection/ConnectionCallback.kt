package com.billysaputra.githubsearch.connection

import retrofit2.Call
import retrofit2.Response

interface ConnectionCallback {
    fun onSuccessResponse(call: Call<*>, response: Response<*>)
    fun onFailedResponse(call: Call<*>, message: String)
    fun onFailure(call: Call<*>)
}