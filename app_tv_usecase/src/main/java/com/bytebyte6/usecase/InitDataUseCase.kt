package com.bytebyte6.usecase

import android.content.Context
import androidx.work.*
import com.bytebyte6.base.RxUseCase
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.Language
import com.bytebyte6.usecase.work.FindImageWork
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class InitDataUseCase(
    private val tvDao: TvDao,
    private val countryDao: CountryDao,
    private val userDao: UserDao,
    private val context: Context,
    private val gson: Gson
) : RxUseCase<Unit, List<Tv>>() {

    override fun run(param: Unit): List<Tv> {

        if (tvDao.getCount() == 0) {
            val tvs = getTvs(context)
            val cs = mutableSetOf<Country>()
            tvs.forEach {
                if (it.language.isEmpty()) {
                    it.language = mutableListOf(Language("Other", "777"))
                }
                if (it.category.isEmpty()) {
                    it.category = "Other"
                }
                it.countryName = it.country.name
                cs.add(it.country)
            }
            countryDao.insert(cs.toList())
            val newTvs = tvs.map {
                val name = it.country.name
                if (name.isNotEmpty()) {
                    it.countryId = countryDao.getIdByName(name)
                }
                it
            }
            tvDao.insert(newTvs)
        }

        if (userDao.getCount() != 0 && userDao.getUser().capturePic) {
            findImageLink()
        }

        return emptyList()
    }

    private fun findImageLink() {
        val workRequest = OneTimeWorkRequestBuilder<FindImageWork>()
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()
        WorkManager.getInstance(context)
            .beginUniqueWork("findImageLink", ExistingWorkPolicy.KEEP, workRequest)
            .enqueue()
    }

    private fun getTvs(context: Context): List<Tv> {
        val json: String = context.assets.open("channels.json")
            .bufferedReader()
            .use { it.readText() }
        return gson.fromJson(json, object : TypeToken<List<Tv>>() {}.type)
    }
}

