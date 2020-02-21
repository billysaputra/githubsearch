package com.billysaputra.githubsearch.helper

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessScrollListener(layoutManager: RecyclerView.LayoutManager, private var visibleThreshold: Int) : RecyclerView.OnScrollListener() {
    /**
     * Noted :
     * visibleThreshold -> minimum amount of item in scrollview before load new data (we called it preparation data)
     * currentPage -> page to load next data
     * previousTotalItemCount -> total item in dataset after last load
     * isLoading -> boolean to indicate that the data is being load, so prevent onLoadMore called more than once
     * */
    private var currentPage = 1
    private var startingPageIndex = 1
    private var previousTotalItemCount = 0
    private var isLoading = true
    private var maxPage: Int = 1

    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    init {
        when (layoutManager) {
            is LinearLayoutManager -> this.mLayoutManager = layoutManager
            is GridLayoutManager -> {
                this.mLayoutManager = layoutManager
                visibleThreshold *= layoutManager.spanCount
            }
            is StaggeredGridLayoutManager -> {
                this.mLayoutManager = layoutManager
                visibleThreshold *= layoutManager.spanCount
            }
        }
        println("visibleThreshold = $visibleThreshold, CurrentPage/MaxPage = $currentPage/$maxPage")
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    /**
     * WARNING !!!
     * onScrolled triggered more than once during scroll, be aware of the code you place here
     * */

    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount

        when (mLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> lastVisibleItemPosition = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            is LinearLayoutManager -> lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        /**
         * Invalidate Dataset, we should reset the recyclerview back to initial state (Rarely happen)
         * */
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) this.isLoading = true
        }

        /**
         * If it’s still loading, we check to see if the dataset count has changed,
         * if so we conclude it has finished loading and update the current page number and total item count
         * Why +1? Add 1 to include dummy data set used for loading indicator by recyclerView
         * */
        if (isLoading && totalItemCount > previousTotalItemCount+1) {
            isLoading = false
            previousTotalItemCount = totalItemCount
        }

        /**
         * If it isn’t currently loading, we check to see if we have breached
         * the visibleThreshold and need to reload more data.
         *
         * If we do need to reload some more data, we execute onLoadMore to fetch the data.
         * threshold should reflect how many total columns there are too
         *
         * Add the scroll limitation when it has reach the last page
         * */
        if (!isLoading && lastVisibleItemPosition + visibleThreshold > totalItemCount && currentPage < maxPage) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            isLoading = true
            println("CurrentPage/MaxPage = $currentPage/$maxPage")
        }
    }

    /**
     * Call this method whenever performing new searches
     * */
    fun resetState(maxPage: Int) {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.isLoading = true
        this.maxPage = maxPage
    }

    /**
     * Use this method to prevent onLoadMore triggered when getting new dataset
     * */
    fun isLoading(isLoading : Boolean){
        this.isLoading = isLoading
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view : RecyclerView)
}