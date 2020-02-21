package com.billysaputra.githubsearch.view

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.billysaputra.githubsearch.R
import com.billysaputra.githubsearch.helper.hideKeyboard

class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private var userName = ""
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
        searchView.queryHint = getString(R.string.search_by_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                userName = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }
}
