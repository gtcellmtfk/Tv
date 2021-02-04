package com.bytebyte6.usecase

import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv

class SearchTvUseCase(private val dataManager: DataManager) : RxUseCase<String, List<Tv>>() {
    override fun run(param: String): List<Tv> {
        return dataManager.getTvsByKeyword(param)
    }
}