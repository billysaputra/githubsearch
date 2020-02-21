package com.billysaputra.githubsearch.view

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.billysaputra.githubsearch.R
import com.billysaputra.githubsearch.adapter.GithubUserListAdapter
import com.billysaputra.githubsearch.helper.hideKeyboard
import com.billysaputra.githubsearch.model.User
import com.billysaputra.githubsearch.view_model.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var mainViewModel : MainViewModel
    private lateinit var githubUserListAdapter : GithubUserListAdapter
    private var userName = ""

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
        mainViewModel.setSearch(userName, 1)
        mainViewModel.getSearch().observe(this, Observer { searchResponse->
            switchProgressBarVisibility(false)
            if(!searchResponse.items.isNullOrEmpty()){
                initAdapter(searchResponse.items)
                switchEmptyLayout(false)
            }else{
                switchEmptyLayout(true)
            }
        })
    }

    private fun initAdapter(userList : ArrayList<User>){
        githubUserListAdapter = GithubUserListAdapter(userList)
        rv_user_list.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        rv_user_list.layoutManager = linearLayoutManager
        rv_user_list.adapter = githubUserListAdapter
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
}
