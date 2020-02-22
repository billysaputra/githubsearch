package com.billysaputra.githubsearch.contract

import com.billysaputra.githubsearch.model.User
import retrofit2.Call

interface MainViewContract{
    interface View {
        fun initAdapter(userList : ArrayList<User>, maxPage : Int)
        fun onLoadMore(nextUserList : ArrayList<User>, maxPage : Int)
        fun onNoConnection()
        fun showSnackbar(message: String)
        fun switchProgressBar(isVisible : Boolean)
        fun switchEmptyLayout(isVisible : Boolean, message : String = "User not found")
    }

    interface Presenter {
        fun searchRequestByUserName(username: String, currentPage: Int)
        fun requestSearchAPI(call : Call<*>)
        fun retryCall()
    }
}