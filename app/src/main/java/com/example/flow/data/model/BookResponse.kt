package com.example.flow.data.model

import com.google.gson.annotations.SerializedName

data class BookResponse (
    @SerializedName("items")
    val items : List<BookItems>
)