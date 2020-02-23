package com.billysaputra.githubsearch.view

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.billysaputra.githubsearch.R
import com.billysaputra.githubsearch.adapter.GithubUserListAdapter
import com.billysaputra.githubsearch.contract.MainViewContract
import com.billysaputra.githubsearch.helper.EndlessScrollListener
import com.billysaputra.githubsearch.helper.hideKeyboard
import com.billysaputra.githubsearch.helper.showSnackbar
import com.billysaputra.githubsearch.model.User
import com.billysaputra.githubsearch.presenter.MainViewPresenter
import com.billysaputra.githubsearch.presenter.MainViewPresenter.Companion.INITIAL_PAGE
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainViewContract.View {
    private lateinit var searchView: SearchView
    private lateinit var githubUserListAdapter : GithubUserListAdapter
    private lateinit var endlessScrollListener: EndlessScrollListener
    private lateinit var userList: ArrayList<User>
    private var userName = ""
    private var mainViewPresenter = MainViewPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_by_user_name)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                userName = query
                mainViewPresenter.searchRequestByUserName(userName, INITIAL_PAGE)
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun initAdapter(userList: ArrayList<User>, maxPage: Int) {
        this.userList = userList
        switchEmptyLayout(false)

        githubUserListAdapter = GithubUserListAdapter(userList)
        rv_user_list.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        rv_user_list.layoutManager = linearLayoutManager
        rv_user_list.adapter = githubUserListAdapter

        endlessScrollListener = object : EndlessScrollListener(linearLayoutManager, 2) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadMoreData(page)
            }
        }
        rv_user_list.addOnScrollListener(endlessScrollListener)
        endlessScrollListener.resetState(maxPage)
    }

    override fun onLoadMore(nextUserList: ArrayList<User>, maxPage: Int) {
        userList.removeAt(userList.size - 1)
        githubUserListAdapter.notifyItemRemoved(userList.size)
        userList.addAll(nextUserList)
        githubUserListAdapter.notifyDataSetChanged()
    }

    override fun onNoConnection() {
        val retrySnackbar = Snackbar.make(rv_user_list, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE)
        retrySnackbar.setAction(R.string.retry) { mainViewPresenter.retryCall() }.show()
    }

    override fun switchProgressBar(isVisible: Boolean) {
        pb_main.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun switchEmptyLayout(isVisible : Boolean, message: String) {
        rv_user_list.visibility = if(isVisible) View.GONE else View.VISIBLE
        iv_failed.visibility =  if(isVisible) View.VISIBLE else View.GONE
        iv_failed.setImageResource(R.drawable.ic_not_found)
        tv_empty_message.visibility =  if(isVisible) View.VISIBLE else View.GONE
        tv_empty_message.text = message
    }

    override fun showSnackbar(message: String) {
        rv_user_list.showSnackbar(message, Snackbar.LENGTH_LONG)
    }

    private fun loadMoreData(page : Int){
        userList.add(User())
        githubUserListAdapter.notifyItemInserted(userList.size - 1)
        mainViewPresenter.searchRequestByUserName(userName, page)
    }
}
