package com.billysaputra.githubsearch.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResponse(
    @SerializedName("total_count") var totalCount: Int = 0,
    @SerializedName("incomplete_results") var incompleteResults: Boolean = false,
    @SerializedName("items") var items: ArrayList<User> = arrayListOf()
) : Parcelable

@Parcelize
data class User(
    @SerializedName("avatar_url") var avatarUrl: String? = "",
    @SerializedName("events_url") var eventsUrl: String? = "",
    @SerializedName("followers_url") var followersUrl: String? = "",
    @SerializedName("following_url") var followingUrl: String? = "",
    @SerializedName("gists_url") var gistsUrl: String? = "",
    @SerializedName("gravatar_id") var gravatarId: String? = "",
    @SerializedName("html_url") var htmlUrl: String? = "",
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("login") var login: String? = "",
    @SerializedName("node_id") var nodeId: String? = "",
    @SerializedName("organizations_url") var organizationsUrl: String? = "",
    @SerializedName("received_events_url") var receivedEventsUrl: String? = "",
    @SerializedName("repos_url") var reposUrl: String? = "",
    @SerializedName("score") var score: Double? = 0.0,
    @SerializedName("site_admin") var siteAdmin: Boolean? = false,
    @SerializedName("starred_url") var starredUrl: String? = "",
    @SerializedName("subscriptions_url") var subscriptionsUrl: String? = "",
    @SerializedName("type") var type: String? = "",
    @SerializedName("url") var url: String? = ""
) : Parcelable