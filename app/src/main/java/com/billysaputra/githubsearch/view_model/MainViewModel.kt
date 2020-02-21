package com.billysaputra.githubsearch.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.billysaputra.githubsearch.connection.ConnectionCallback
import com.billysaputra.githubsearch.connection.ConnectionManager
import com.billysaputra.githubsearch.connection.RetrofitService
import com.billysaputra.githubsearch.model.SearchResponse
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {
    private var searchResponse = MutableLiveData<SearchResponse>()
    var errorMessage = MutableLiveData<String>()

    companion object {
        const val PER_PAGE = 10
        const val INITIAL_PAGE = 1
        const val VISIBLE_THRESHOLD = 2
        const val NO_CONNECTION = "NO_CONNECTION"
        const val API_FAILURE = "API_FAILURE"
    }

    internal fun setSearch(userName : String, currentPage: Int){
        val requestSearch =  RetrofitService.request.searchUser(userName, currentPage, PER_PAGE)
        val connectionManager = ConnectionManager()
        connectionManager.connect(requestSearch, object : ConnectionCallback{
            override fun onSuccessResponse(call: Call<*>, response: Response<*>) {
                val responseSearch = response.body() as SearchResponse
                searchResponse.postValue(responseSearch)
            }

            override fun onFailedResponse(call: Call<*>, message: String) {
                errorMessage.postValue(API_FAILURE)
            }

            override fun onFailure(call: Call<*>) {
                errorMessage.postValue(NO_CONNECTION)
            }
        })
    }

    internal fun getSearch(): LiveData<SearchResponse>{
        return searchResponse
    }
}