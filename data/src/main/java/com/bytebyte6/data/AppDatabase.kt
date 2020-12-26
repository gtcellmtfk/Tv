package com.bytebyte6.data

import androidx.room.*
import com.bytebyte6.data.model.IpTv

@Database(entities = [IpTv::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ipTvDao(): IpTvDao
}

