package com.billysaputra.githubsearch.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message") var message: String? = "",
    @SerializedName("documentation_url") var documentationUrl: String = "")