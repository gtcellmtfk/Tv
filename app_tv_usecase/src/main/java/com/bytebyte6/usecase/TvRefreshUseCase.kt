package com.bytebyte6.usecase

import com.bytebyte6.data.TvApi
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.model.Language
import io.reactivex.rxjava3.core.Single

/**
 * 更新本地数据库数据
 */
class TvRefreshUseCase(private val api: TvApi, private val tvDao: TvDao) {
    fun getSingle(): Single<Boolean> {
        return api.getTvs().map { list ->
            tvDao.insert(list.map {
                if (it.category.isEmpty()) {
                    it.category = "Other"
                }
                if (it.language.isEmpty()) {
                    it.language = mutableListOf(Language("Other", "777"))
                }
                it.countryName = it.country.name
                it
            })
            true
        }
    }
}