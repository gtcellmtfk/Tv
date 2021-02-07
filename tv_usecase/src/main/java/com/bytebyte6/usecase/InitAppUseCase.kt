package com.bytebyte6.usecase

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.bytebyte6.common.Result
import com.bytebyte6.common.RxUseCase2
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Language
import com.bytebyte6.usecase.work.FindImageWork
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.annotations.TestOnly

interface InitAppUseCase : RxUseCase2<Unit, List<Tv>>

class InitAppUseCaseImpl(
    private val dataManager: DataManager,
    private val context: Context? = null,
    private val gson: Gson
) : InitAppUseCase {

    override val result: MutableLiveData<Result<List<Tv>>> = MutableLiveData()

    private var tvs: List<Tv>? = null

    @TestOnly
    fun setTvs(tvs: List<Tv>) {
        this.tvs = tvs
    }

    override fun run(param: Unit): List<Tv> {

        dataManager.getCurrentUserIfNotExistCreate()

        if (dataManager.getTvCount() == 0) {
            if (tvs == null) {
                tvs = getTvs(context!!)
            }
            val cs = mutableSetOf<Country>()
            tvs!!.forEach {
                if (it.language.isEmpty()) {
                    it.language = mutableListOf(Language.DEFAULT)
                }
                if (it.category.isEmpty()) {
                    it.category = Category.OTHER
                }
                it.countryName = it.country.name
                cs.add(it.country)
            }
            dataManager.insertCountry(cs.toList())
            val newTvs = tvs!!.map {
                val name = it.country.name
                if (name.isNotEmpty()) {
                    //实体关联
                    it.countryId = dataManager.getCountryIdByName(name)
                }
                it
            }
            dataManager.insertTv(newTvs)
        }

        if (dataManager.getTvCount() != 0 && dataManager.getCurrentUserIfNotExistCreate().capturePic) {
            findImageLink()
        }

        tvs = null

        return emptyList()
    }

    private fun findImageLink() {
        context?.let {
            val workRequest = OneTimeWorkRequestBuilder<FindImageWork>()
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .build()
            WorkManager.getInstance(it)
                .beginUniqueWork("findImageLink", ExistingWorkPolicy.KEEP, workRequest)
                .enqueue()
        }
    }

    private fun getTvs(context: Context): List<Tv> {
        val json: String = context.assets.open("channels.json")
            .bufferedReader()
            .use { it.readText() }
        return gson.fromJson(json, object : TypeToken<List<Tv>>() {}.type)
    }
}

