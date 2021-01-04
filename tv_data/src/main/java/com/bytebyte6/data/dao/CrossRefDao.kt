package com.bytebyte6.data.dao

import androidx.room.Dao
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.UserPlaylistCrossRef

@Dao
abstract class PlaylistTvCrossRefDao : BaseDao<PlaylistTvCrossRef>

@Dao
abstract class UserPlaylistCrossRefDao : BaseDao<UserPlaylistCrossRef>