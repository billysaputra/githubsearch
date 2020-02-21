package com.billysaputra.githubsearch.connection

import android.util.Log
import com.billysaputra.githubsearch.helper.enqueue
import retrofit2.Call
import retrofit2.Response

class ConnectionManager {
    private val TAG = ConnectionManager::class.java.simpleName
    private lateinit var mConnectionCallback: ConnectionCallback
    private lateinit var mCall: Call<*>

    fun connect(mCall: Call<*>, mConnectionCallback: ConnectionCallback) {
        this.mCall = mCall
        this.mConnectionCallback = mConnectionCallback
        callAPIRequest()
    }

    private fun callAPIRequest() {
        mCall.clone().enqueue {
            onResponse = { call: Call<out Any>, response: Response<out Any> ->
                if (response.isSuccessful) {
                    Log.i(TAG, "onSuccess, URL : ${call.request().url()}")
                    mConnectionCallback.onSuccessResponse(call, response)
                } else {
                    Log.i(TAG, "onFailed, URL : ${call.request().url()}")
                    println("Response : ${response.errorBody()}")
                    mConnectionCallback.onFailedResponse(call, response.message())
                }

            }
            onFailure = { call: Call<out Any>, t: Throwable? ->
                Log.i(TAG, "onFailure, URL: ${call.request().url()}")
                mConnectionCallback.onFailure(call)
            }
        }
    }
}