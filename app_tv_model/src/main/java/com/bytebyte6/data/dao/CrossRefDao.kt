package com.bytebyte6.data.dao

import androidx.room.Dao
import com.bytebyte6.data.entity.PlaylistTvCrossRef
import com.bytebyte6.data.entity.UserPlaylistCrossRef

@Dao
interface PlaylistTvCrossRefDao : BaseDao<PlaylistTvCrossRef>

@Dao
interface UserPlaylistCrossRefDao : BaseDao<UserPlaylistCrossRef>