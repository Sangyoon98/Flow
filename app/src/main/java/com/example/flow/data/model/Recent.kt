package com.example.flow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recent (var search : String, var createdTime : Long = System.currentTimeMillis()) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}