package com.billysaputra.githubsearch.helper

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

object Utils {
    fun glideRequestOptions(placeholder: Int): RequestOptions {
        return RequestOptions()
            .placeholder(placeholder)
            .error(placeholder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .dontAnimate()
            .dontTransform()
    }
}