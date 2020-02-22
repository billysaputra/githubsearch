package com.billysaputra.githubsearch.presenter

import com.billysaputra.githubsearch.connection.ConnectionCallback
import com.billysaputra.githubsearch.connection.ConnectionManager
import com.billysaputra.githubsearch.connection.RetrofitService
import com.billysaputra.githubsearch.contract.MainViewContract
import com.billysaputra.githubsearch.model.SearchResponse
import retrofit2.Call
import retrofit2.Response
import kotlin.math.ceil

class MainViewPresenter(private val mainView: MainViewContract.View) : MainViewContract.Presenter {
    private lateinit var searchUserRequest: Call<*>
    private var currentPage = 0

    companion object {
        const val PER_PAGE = 10
        const val INITIAL_PAGE = 1
    }

    override fun searchRequestByUserName(username: String, currentPage: Int) {
        this.currentPage = currentPage
        if (currentPage == INITIAL_PAGE) mainView.switchProgressBar(true)
        searchUserRequest = RetrofitService.request.searchUser(username, currentPage, PER_PAGE)
        requestSearchAPI(searchUserRequest)
    }

    override fun requestSearchAPI(call: Call<*>) {
        val connectionManager = ConnectionManager()
        connectionManager.connect(searchUserRequest, object : ConnectionCallback {
            override fun onSuccessResponse(call: Call<*>, response: Response<*>) {
                val searchResponse = response.body() as SearchResponse
                if (!searchResponse.items.isNullOrEmpty() && currentPage == INITIAL_PAGE) {
                    mainView.initAdapter(searchResponse.items, getMaxPage(searchResponse.totalCount))
                } else if (!searchResponse.items.isNullOrEmpty() && currentPage >= INITIAL_PAGE) {
                    mainView.onLoadMore(searchResponse.items, getMaxPage(searchResponse.totalCount))
                } else {
                    mainView.switchEmptyLayout(true)
                }
                mainView.switchProgressBar(false)
            }

            override fun onFailedResponse(call: Call<*>, message: String) {
                mainView.switchProgressBar(false)
                if (currentPage == INITIAL_PAGE) mainView.switchEmptyLayout(true, message)
                else mainView.showSnackbar(message)
            }

            override fun onFailure(call: Call<*>) {
                mainView.switchProgressBar(false)
                mainView.onNoConnection()
            }
        })
    }

    override fun retryCall() {
        if (currentPage == INITIAL_PAGE) mainView.switchProgressBar(true)
        requestSearchAPI(searchUserRequest)
    }

    private fun getMaxPage(totalCount: Int): Int {
        return ceil(totalCount.toDouble() / PER_PAGE).toInt()
    }
}