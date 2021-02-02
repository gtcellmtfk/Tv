package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.data.entity.User
import com.bytebyte6.data.model.UserWithPlaylists

object FakeUserDao : UserDao {
    override fun getUsersWithPlaylists(): List<UserWithPlaylists> {
        return emptyList()
    }

    override fun getPlaylistsByUserId(userId: Long): UserWithPlaylists {
        return UserWithPlaylists(defaultUser)
    }

    override fun getUsers(): List<User> {
        return emptyList()
    }

    var defaultUser = User(name = "Admin")

    override fun getUser(): User {
        return defaultUser
    }

    override fun getCount(): Int {
        return 0
    }

    override fun user(): LiveData<User> {
        return MutableLiveData(defaultUser)
    }

    override fun userWithPlaylists(userId: Long): LiveData<UserWithPlaylists> {
        return MutableLiveData(UserWithPlaylists(defaultUser))
    }

    override fun insert(data: User): Long {
        defaultUser = data
        return 1
    }

    override fun insert(list: List<User>): List<Long> {
        return emptyList()
    }

    override fun delete(data: User) {

    }

    override fun delete(list: List<User>) {

    }

    override fun update(data: User) {

    }

    override fun update(list: List<User>) {

    }
}