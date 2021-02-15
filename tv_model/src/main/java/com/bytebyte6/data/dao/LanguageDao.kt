package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bytebyte6.data.entity.Language

@Dao
interface LanguageDao : BaseDao<Language> {
    @Query("SELECT COUNT(*) FROM Language")
    fun getCount(): Int

    /**
     * LiveData
     */
    @Query("SELECT * FROM Language ORDER BY languageName ASC")
    fun languages(): LiveData<List<Language>>
}