package com.billysaputra.githubsearch.view

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.billysaputra.githubsearch.R
import com.billysaputra.githubsearch.adapter.GithubUserListAdapter
import com.billysaputra.githubsearch.helper.EndlessScrollListener
import com.billysaputra.githubsearch.helper.hideKeyboard
import com.billysaputra.githubsearch.helper.showSnackbar
import com.billysaputra.githubsearch.model.SearchResponse
import com.billysaputra.githubsearch.model.User
import com.billysaputra.githubsearch.view_model.MainViewModel
import com.billysaputra.githubsearch.view_model.MainViewModel.Companion.INITIAL_PAGE
import com.billysaputra.githubsearch.view_model.MainViewModel.Companion.PER_PAGE
import com.billysaputra.githubsearch.view_model.MainViewModel.Companion.VISIBLE_THRESHOLD
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var mainViewModel : MainViewModel
    private lateinit var githubUserListAdapter : GithubUserListAdapter
    private lateinit var endlessScrollListener: EndlessScrollListener
    private lateinit var searchResponse: SearchResponse
    private var userName = ""
    private var currentPage = INITIAL_PAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_by_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                currentPage = INITIAL_PAGE
                beginSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    private fun beginSearch(userName : String){
        switchProgressBarVisibility(true)
        this.userName = userName
        mainViewModel.setSearch(userName, currentPage)
        mainViewModel.getSearch().observe(this, Observer { response->
            switchProgressBarVisibility(false)

            if(currentPage == INITIAL_PAGE){
                searchResponse = response
                if(!searchResponse.items.isNullOrEmpty()){
                    initAdapter()
                    switchEmptyLayout(false)
                }else{
                    switchEmptyLayout(true)
                }
            }else{
                if(!searchResponse.items.isNullOrEmpty()){
                    println("Search Response Before Size :${searchResponse.items.size}")
                    searchResponse.items.removeAt(searchResponse.items.size - 1)
                    githubUserListAdapter.notifyItemRemoved(searchResponse.items.size)
                    searchResponse.items.addAll(response.items)
                    githubUserListAdapter.notifyDataSetChanged()
                    println("Search Response After Size :${searchResponse.items.size}")
                    switchEmptyLayout(false)
                }else{
                    switchEmptyLayout(true)
                }
            }
        })

        mainViewModel.errorMessage.observe(this, Observer { errorMessage ->
            rv_user_list.showSnackbar(errorMessage, Snackbar.LENGTH_LONG)
        })
    }

    private fun initAdapter(){
        githubUserListAdapter = GithubUserListAdapter(searchResponse.items)
        rv_user_list.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        rv_user_list.layoutManager = linearLayoutManager
        rv_user_list.adapter = githubUserListAdapter

        endlessScrollListener = object : EndlessScrollListener(linearLayoutManager, VISIBLE_THRESHOLD) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                println("OnLoadMorePage @:$page")
                loadMore(userName, page)
            }
        }
        rv_user_list.addOnScrollListener(endlessScrollListener)
        endlessScrollListener.resetState(getMaxPage(searchResponse.totalCount))
    }

    private fun switchProgressBarVisibility(isVisible : Boolean){
        pb_main.visibility = if(isVisible) View.VISIBLE else View.GONE
        rv_user_list.visibility = View.GONE
        iv_failed.visibility = View.GONE
        tv_empty_message.visibility = View.GONE
    }

    private fun switchEmptyLayout(isVisible: Boolean){
        pb_main.visibility = View.GONE
        rv_user_list.visibility = if(isVisible) View.GONE else View.VISIBLE
        iv_failed.visibility = if(isVisible) View.VISIBLE else View.GONE
        tv_empty_message.visibility = if(isVisible) View.VISIBLE else View.GONE
    }

    private fun loadMore(userName : String, page : Int){
        this.currentPage = page
        searchResponse.items.add(User())
        githubUserListAdapter.notifyItemInserted(searchResponse.items.size - 1)
        mainViewModel.setSearch(userName, page)
    }

    private fun getMaxPage(totalCount : Int): Int {
        return ceil(totalCount.toDouble() / PER_PAGE).toInt()
    }
}
