package com.example.test_ui_factory.entry

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
data class User(
    @PrimaryKey
    val id: Long = 0L,
)