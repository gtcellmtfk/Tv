package com.bytebyte6.usecase

import androidx.annotation.Keep
import com.bytebyte6.common.RxUseCase
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Tv

@Keep
data class UpdateTvParam(
    var pos: Int,
    var tv: Tv
)