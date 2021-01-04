package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long=0,
    var name: String
) : Parcelable