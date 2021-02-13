package com.bytebyte6.usecase

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.bytebyte6.common.Result
import com.bytebyte6.common.RxUseCase2
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.usecase.work.FindImageWork
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.annotations.TestOnly

interface InitAppUseCase : RxUseCase2<Unit, User>

class InitAppUseCaseImpl(
    private val dataManager: DataManager,
    private val context: Context? = null
) : InitAppUseCase {

    override val result: MutableLiveData<Result<User>> = MutableLiveData()

    override fun run(param: Unit): User {

        val user = dataManager.getCurrentUserIfNotExistCreate()

        if (dataManager.getTvCount() != 0 && user.capturePic) {
            findImageLink()
        }

        return user
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
}

