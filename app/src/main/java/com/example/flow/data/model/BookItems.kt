package com.example.flow.data.model

import com.google.gson.annotations.SerializedName

data class BookItems(
    @SerializedName("link")
    val link : String,

    @SerializedName("image")
    val image : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("author")
    val author : String,

    @SerializedName("publisher")
    val publisher : String,

    @SerializedName("discount")
    val discount : String
)
