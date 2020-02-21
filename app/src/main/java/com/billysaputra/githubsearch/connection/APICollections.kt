package com.billysaputra.githubsearch.connection

import com.billysaputra.githubsearch.helper.Constants
import com.billysaputra.githubsearch.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APICollections {
    @GET(Constants.URL_API.SEARCH_USER)
    fun searchUser(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<SearchResponse>
}