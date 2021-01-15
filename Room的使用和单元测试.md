# 一对多关系映射

如国家对电视台

实体Tv，其中@Ignore字段代表忽略该字段，不储存在数据库中，在此项目中仅仅用在反序列化。

```kotlin
@Parcelize
@Entity(
    indices = [
        Index(value = ["tvId"]),
        Index(value = ["url"] , unique = true)
    ]
)
data class Tv(
    @PrimaryKey(autoGenerate = true)
    var tvId: Long = 0,
    var url: String = "",
    var category: String = "",
    var logo: String = "",
    var name: String = "",
    var language: List<Language> = emptyList(),
    @Ignore
    var country: Country = Country(),
    
    // 此处为一对多关系的关键，把Tv对象插入到数据库时，须将此id设置为相对应国家id
    var countryId: Long = 0,
    var countryName: String = country.name
) : Parcelable
```

实体Country，将实体插入数据库后获得自动生成的主键赋值给Tv实体中的countryId。

```kotlin
@Parcelize
@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
data class Country(
    @PrimaryKey(autoGenerate = true)
    val countryId: Long = 0,
    var name: String = "",
    var images: List<String> = emptyList()
) : Parcelable
```

Dao查询

```kotlin
@Dao
interface CountryDao{
    @Transaction
    @Query("SELECT * FROM Country")
    fun getCountryWithTvss(): List<CountryWithTvs>

    @Transaction
    @Query("SELECT * FROM Country WHERE countryId=:countryId")
    fun getCountryWithTvs(countryId: Long): CountryWithTvs

    @Transaction
    @Query("SELECT * FROM Country WHERE countryId=:countryId")
    fun countryWithTvs(countryId: Long): LiveData<CountryWithTvs>

    @Query("SELECT * FROM Country ORDER BY name ASC")
    fun countries(): LiveData<List<Country>>
}
```

# 多对多关系映射

如电视台和播放列表，此项目中播放列表包含多个电视台，一个电视台可能存在于多个播放列表中。

播放列表实体Playlist

```kotlin
@Parcelize
@Entity(indices = [Index(value = ["playlistName"], unique = true)])
@Keep
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    var playlistName: String
) : Parcelable
```

实体交叉，使用dao的insert方法将PlaylistTvCrossRef插入数据库进行关联，查询时需加上@Transaction注解，代表这是个原子操作。

```kotlin
@Entity(primaryKeys = ["playlistId", "tvId"] ,indices = [Index("tvId")])
@Keep
//此实体会将播放列表和电视台关联起来
data class PlaylistTvCrossRef(
    val playlistId: Long,
    val tvId: Long
)

@Dao
interface PlaylistTvCrossRefDao : BaseDao<PlaylistTvCrossRef>

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<T>): List<Long>
}

@Test
fun tvOneToMany() {
    val playlists = playlistDao.getPlaylists()
    val tvs = tvDao.getTvs()

    val a = PlaylistTvCrossRef(playlists[0].playlistId, tvs[0].tvId)
    val b = PlaylistTvCrossRef(playlists[1].playlistId, tvs[0].tvId)
    val c = PlaylistTvCrossRef(playlists[2].playlistId, tvs[0].tvId)

    playlistTvCrossRefDao.insert(mutableListOf(a, b, c))
    
    val list = tvDao.getTvWithPlaylistss()
    
    assert(list[0].tv.tvId == tvs[0].tvId)
    assert(list[0].playlists.size == 3)
}
```

```kotlin
@Parcelize
@Keep
//查询播放列表和对应的电视台
data class PlaylistWithTvs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "tvId",
        associateBy = Junction(PlaylistTvCrossRef::class)
    )
    val tvs: List<Tv> = emptyList()
) : Parcelable
```

```kotlin
@Parcelize
@Keep
//查询电视台和对应的播放列表
data class TvWithPlaylists(
    @Embedded val tv: Tv,
    @Relation(
        parentColumn = "tvId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistTvCrossRef::class)
    )
    val playlists: List<Playlist> = emptyList()
) : Parcelable
```

Dao查询

```kotlin
@Transaction
@Query("SELECT * FROM Playlist")
fun getPlaylistsWithTvss(): List<PlaylistWithTvs>

@Transaction
@Query("SELECT * FROM Playlist WHERE playlistId=:playlistId")
fun getPlaylistWithTvsById(playlistId: Long): PlaylistWithTvs

@Transaction
@Query("SELECT * FROM Tv")
fun getTvWithPlaylistss(): List<TvWithPlaylists>
```

# Fts全文检索

定义一个新的实体

```kotlin
@Parcelize
@Fts4(contentEntity = Tv::class)
@Entity
@Keep
data class TvFts(
    val tvId: Long,
    var url: String,
    var category: String = "",
    var logo: String = "",
    var name: String = "",
    var countryName: String = "",
    var language: String = ""
) : Parcelable 
```

Dao查询

```kotlin
@Dao
interface TvFtsDao {
    @Query("SELECT * FROM TvFts WHERE TvFts MATCH :key ")
    fun search(key: String): List<TvFts>

    @Query("SELECT COUNT(*) FROM TvFts WHERE TvFts MATCH :key ")
    fun getCount(key: String): Int

    @Query("SELECT * FROM TvFts WHERE TvFts MATCH :key LIMIT $PAGE_SIZE OFFSET :offset")
    fun paging(offset: Int, key: String): List<TvFts>
    
    @Query("SELECT COUNT(*) FROM TvFts WHERE TvFts MATCH :key ")
    fun count(key: String): LiveData<Int>
}
```

# 单元测试

使用Room.inMemoryDatabaseBuilder构建基于内存的数据库

```kotlin
@RunWith(AndroidJUnit4::class)
class CountryDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var countryDao: CountryDao
    private lateinit var context: Context

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        countryDao = db.countryDao()
    }

    @After
    fun closeDb() {
        db.close()
    }
    
    @Test
    fun test_getCount() {
        assert(countryDao.getCount() == 0)
        countryDao.insert(china)
        assert(countryDao.getCount() == 1)
    }
}
```

# [详细内容](https://github.com/bytebyte6/Rtmp/tree/master/app_tv_model/src/main/java/com/bytebyte6/data)