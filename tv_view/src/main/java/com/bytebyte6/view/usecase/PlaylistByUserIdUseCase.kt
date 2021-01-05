package com.bytebyte6.view.usecase

import com.bytebyte6.data.RxUseCase
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.entity.UserWithPlaylists
import io.reactivex.rxjava3.core.Single

class PlaylistByUserIdUseCase(
    private val userDao: UserDao
) : RxUseCase<Long, UserWithPlaylists>() {
    override fun getSingle(param: Long): Single<UserWithPlaylists> =
        Single.create {
            it.onSuccess(userDao.getPlaylistsByUserId(userDao.get().userId))
        }
}