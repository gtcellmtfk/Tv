package com.bytebyte6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bytebyte6.data.entity.Category
import com.bytebyte6.data.entity.Language

@Dao
interface CategoryDao : BaseDao<Category> {
    @Query("SELECT COUNT(*) FROM Category")
    fun getCount(): Int

    /**
     * LiveData
     */
    @Query("SELECT * FROM Category ORDER BY category ASC")
    fun categories(): LiveData<List<Category>>
}