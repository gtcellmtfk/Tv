package com.bytebyte6.data

import androidx.room.Room
import org.koin.dsl.module

/**
 * 测试专用
 */
val roomMemoryModule = module {
    single { Room.inMemoryDatabaseBuilder(get(), AppDatabase::class.java).build() }
}
