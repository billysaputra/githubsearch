package com.billysaputra.githubsearch.connection

import com.billysaputra.githubsearch.helper.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService {
    companion object {
        private const val REQUEST_TIMEOUT = 10000L
        private val gson = GsonBuilder().setLenient().create()
        private val okHttpClient = OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()

        val request: APICollections by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

            retrofit.create(APICollections::class.java)
        }
    }
}