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

    companion object {
        const val PER_PAGE = 10
        const val INITIAL_PAGE = 1
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

            }

            override fun onFailure(call: Call<*>) {

            }
        })
    }

    internal fun getSearch(): LiveData<SearchResponse>{
        return searchResponse
    }
}