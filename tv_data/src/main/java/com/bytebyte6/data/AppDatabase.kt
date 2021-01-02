package com.bytebyte6.data

import androidx.room.*
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.data.model.IpTvFts

@Database(entities = [IpTv::class,IpTvFts::class], version = 1, exportSchema = true)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ipTvDao(): IpTvDao
    abstract fun ipTvFtsDao(): IpTvFtsDao
}

