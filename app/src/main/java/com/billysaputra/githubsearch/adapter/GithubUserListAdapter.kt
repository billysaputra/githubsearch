package com.billysaputra.githubsearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.billysaputra.githubsearch.R
import com.billysaputra.githubsearch.helper.Utils
import com.billysaputra.githubsearch.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_progress_list.view.*
import kotlinx.android.synthetic.main.item_user_list.view.*

class GithubUserListAdapter(var userList: List<User>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_LOADING = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER -> UserViewHolder(
                layoutInflater.inflate(
                    R.layout.item_user_list,
                    parent,
                    false
                )
            )
            else -> LoadingViewHolder(
                layoutInflater.inflate(
                    R.layout.item_progress_list,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (userList[position].id != 0) VIEW_TYPE_USER else VIEW_TYPE_LOADING
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) {
            holder.setView(userList[position])
        } else if (holder is LoadingViewHolder) {
            holder.setProgressBar()
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setView(user: User) {
            with(itemView) {
                Glide.with(iv_user_avatar.context).load(user.avatarUrl)
                    .apply(Utils.glideRequestOptions(R.drawable.ic_not_found))
                    .into(iv_user_avatar)
                tv_user_name.text = user.login
            }
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setProgressBar() {
            itemView.progressBar.isIndeterminate = true
        }
    }
}