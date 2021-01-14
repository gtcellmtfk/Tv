package com.bytebyte6.view.usecase

import com.bytebyte6.data.RxSingleUseCase
import com.bytebyte6.data.dao.CountryDao
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.work.ImageSearch
import io.reactivex.rxjava3.core.Single

class CountryImageSearchUseCase(
    private val imageSearch: ImageSearch,
    private val countryDao: CountryDao
) : RxSingleUseCase<Country, Boolean>() {
    override fun getSingle(country: Country): Single<Boolean> {
        return Single.create { emitter ->
            if (country.images.isEmpty() && country.name.isNotEmpty()) {
                val images = imageSearch.search(
                    country.name
                        .replace(" ", "+")
                        .plus("+flag+国旗")
                )
                country.images = images
                countryDao.insert(country)
            }
            emitter.onSuccess(true)
        }
    }
}