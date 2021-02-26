package com.bytebyte6.usecase

import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager

/**
 * 设置该Tv为直播源
 */
class LiveContentUseCase(private val dataManager: DataManager) :
    RxUseCase<UpdateTvParam, UpdateTvParam>() {
    override fun run(param: UpdateTvParam): UpdateTvParam {
        param.tv.liveContent = true
        dataManager.updateTv(param.tv)
        return param
    }
}