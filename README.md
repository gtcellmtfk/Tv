# Android M3u-Tv 播放器

> **一款播放[Iptv资源](https://github.com/iptv-org/iptv)的播放器，导入M3u格式的文件，支持下载点播文件。**
>
> **项目使用Mvvm-UseCase架构，Kotlin为开发语言，Room数据库，RxJava异步框架，Koin依赖注入。**
>
> **使用Material Design 组件开发界面，构建过渡动画。**
>
> **项目地址：[gitee](https://gitee.com/bytebyte6/Tv)  [github](https://github.com/bytebyte6/Tv)**

# 功能

- 首页-按国家地区、语言、类型浏览直播源-搜索
- 导入-解析M3u文件-删除列表
- 收藏-取消收藏
- 下载-缓存点播文件-删除缓存
- 设置
- 关于
- 直播源播放
- [直播源推荐](https://github.com/iptv-org/iptv)
- [Download apk](https://gitee.com/bytebyte6/Tv/raw/store-tv/tv/labtest/tv-labtest.apk)

# 界面概览
<img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/1.png" width="320" height="640"><img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/2.png" width="320" height="640"><img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/3.png" width="320" height="640">

<img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/import.png" width="320"  height="640"><img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/fav.png" width="320" height="640"> <img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/setting.png" width="320" height="640">

<img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/fav.png" width="320" height="640"><img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/download.png" width="320" height="640">

<img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/menu.png" width="320" height="640"><img src="https://gitee.com/bytebyte6/blog/raw/master/Tv/search.png" width="320" height="640">



# 数据层

1、[什么是M3u文件](https://baike.baidu.com/item/m3u%E6%96%87%E4%BB%B6/365977)

M3u文件是纯文本文件，文件里保存这资源的地址，以#EXTINF开头，逗号后面是资源的名称（如下面，AM，资源的名称就是AM），紧接着下一行是资源的地址，以下是Github上一个开源的iptv资源文件，比正常m3u文件多了一些信息（tvg-name、tvg-logo...）。

```
#EXTM3U
#EXTINF:-1 tvg-id="" tvg-name="AM" tvg-country="AD" tvg-language="Spanish" tvg-logo="https://graph.facebook.com/rtva.andorra/picture?width=320&height=320" group-title="",AM
https://videos.rtva.ad/live/am/playlist.m3u8
#EXTINF:-1 tvg-id="" tvg-name="RNA" tvg-country="AD" tvg-language="Spanish" tvg-logo="https://graph.facebook.com/rtva.andorra/picture?width=320&height=320" group-title="",RNA
http://videos.rtva.ad:1935/live/rna/playlist.m3u8
#EXTINF:-1 tvg-id="" tvg-name="" tvg-country="AD" tvg-language="Catalan" tvg-logo="https://i.imgur.com/kJCjeQ4.png" group-title="",RTVA (720p)
http://videos.rtva.ad:1935/live/web/playlist.m3u8
```

2、如何解析M3u文件

利用#EXTINF分割字符串

利用\n分割字符串（有一些文件是\r\n，要替换为\n）

```kotlin
private fun getM3uString(byteArray: ByteArray): String {
    return byteArray.commonToUtf8String()
        .removePrefix("#EXTM3U")
        .replace("\r\n", "\n")
        .trim()
}
```

利用正则表达式解析tvg属性

```kotlin
//获取logo的正则表达式
private const val logoPattern = "(?<=tvg-logo=\").*?(?=\")"
private val logoRegex = Regex(logoPattern)
```

[具体实现 M3u.kt](https://gitee.com/bytebyte6/Tv/blob/master/tv_model/src/main/java/com/bytebyte6/data/M3u.kt)

3、Room实体定义

```kotlin
@Parcelize
@Entity
@Keep
data class Tv(
    @PrimaryKey(autoGenerate = true)
    var tvId: Long = 0,
    var url: String = "",
    var category: String = "",
    var logo: String = "",
    var name: String = "",
    var favorite: Boolean = false,
    var language: String = "",
    var countryId: Long = 0,
    var countryName: String = "",
    var countryCode: String = ""
) : Parcelable
```

4、M3u.kt单元测试

```kotlin
@Test
fun test_m3u_parse() {
    // ..代表当前目录的父目录
    // 父目录下的channels文件夹，里面包含用于测试的m3u格式的文件
    val modelDir = File("..", "channels")
    val listFiles = modelDir.listFiles()
    listFiles?.forEach { file ->
        val tvs = M3u.getTvs(file)
        println("${file.name} size = ${tvs.size}")
        tvs.forEach {
            assert(it.name.isNotEmpty())
            assert(it.countryCode.isNotEmpty())
            assert(it.url.isNotEmpty())
            assert(it.language.isNotEmpty())
            assert(it.category.isNotEmpty())
        }
    }
    println("Total File: = ${listFiles?.size}")
}
```

5、[Room实体关系映射](https://gitee.com/bytebyte6/Tv/blob/master/Room%E5%92%8C%E5%8D%95%E5%85%83%E6%B5%8B%E8%AF%95.md)

6、[Room Dao定义](https://gitee.com/bytebyte6/Tv/tree/master/tv_model/src/main/java/com/bytebyte6/data/dao)

7、DataManager的定义，提供数据的增删改查方法

所有数据操作交给DataManager处理

```kotlin
interface DataManager {
    //User
    fun insertUser(user: User): Long
    fun insertUser(users: List<User>): List<Long>
    fun deleteUser(user: User)
    fun updateUser(user: User)
    fun getCurrentUserIfNotExistCreate(): User
    fun hasUser(): Boolean
    fun user(): LiveData<User>
    fun getUsers(): List<User>

    //Language
    fun insertLanguages(languages: List<Language>)
    fun getLangCount(): Int
    
    //...
}
```

8、Koin依赖注入

```kotlin
val dataModule = module {
    // 提供DataManager的单例
    single<DataManager> { DataManagerImpl(get(AppDatabase::class)) }
}
```

9、单元测试

对DataManager的单元测试

[多对多关系测试](https://gitee.com/bytebyte6/Tv/blob/master/tv_model/src/androidTest/java/com/bytebyte6/data/CrossRefTest.kt)

[增删改查测试](https://gitee.com/bytebyte6/Tv/blob/master/tv_model/src/androidTest/java/com/bytebyte6/data/DataManagerTest.kt)

# UseCase层

1、什么是UseCase

- 替ViewModel分担逻辑，减少ViewModel层级代码。
- 如请求一个接口是一个UseCase，收藏一个直播源也是，解析m3u文件也是。
- 有点像Runnable的意思，处理某个任务，或者用户的某个操作。

2、RxUseCase抽象和Result密封类

RxUseCase就是使用RxJava的方式去实现UseCase

```kotlin
abstract class RxUseCase<I, O> {

    // UseCase的结果 
    // Success Error Loading
    private val result: MutableLiveData<Result<O>> = MutableLiveData()

    fun result(): LiveData<Result<O>> = result

    // 提交一个UseCase
    fun execute(param: I): Single<O> = Single.create<O> {
        try {
            result.postValue((Result.Loading()))
            val o = run(param)
            result.postValue((Result.Success(o)))
            it.onSuccess(o)
        } catch (e: Exception) {
            result.postValue((Result.Error(e)))
            it.onError(e)
        }
    }
	
    abstract fun run(param: I): O
}
```


```kotlin
sealed class Result<out R> {
    // 代表这个结果是否已被处理，一些UI事件会用到这个字段，如showDialog，showToast，showSnackbar
    var handled: Boolean = false

    data class Success<out T>(
        val data: T,
        // 加载更多的情况下使用表示数据全部加载完成，用于加载列表数据的情况，其他情况忽略
        val end: Boolean = false
    ) : Result<T>()

    data class Error(
        val error: Throwable
    ) : Result<Nothing>()

    object Loading : Result<Nothing>()
}
```

反复执行某个任务的UseCase

```kotlin
abstract class IntervalUseCase<I,O> : RxUseCase<I,O>(){
    fun interval(param: I, period: Long = 2): Observable<Long> =
        Observable.interval(period, TimeUnit.SECONDS)
            .doOnNext {
                val o = run(param)
                result.postValue((Result.Success(o)))
            }.doOnError {
                result.postValue((Result.Error(it)))
            }
}
```

3、UseCase的单元测试

UseCase的param参数在设计的时候要考虑到测试用例的编写，比如解析M3u文件的时候，用户选择了一个m3u文件并返回了一个Uri，不要将这个Uri直接作为参数，应当定义一个data class，如:

[ParseM3uUseCase.kt](https://gitee.com/bytebyte6/Tv/blob/master/tv_usecase/src/main/java/com/bytebyte6/usecase/ParseM3uUseCase.kt)

```kotlin
@Keep
data class ParseM3uParam(
    // 用户选择后获得的Uri
    val uri: Uri? = null,
    // assets文件夹下的文件名，创建Firebase测试版本apk的时候会打包一个index.m3u的文件到apk中
    val assetsFileName: String? = null,
    // 只在测试中使用到
    val forTest: List<Tv>? = null
)
```

# ViewModel层

1、提交RxUseCase

定义BaseViewModel

```kotlin
abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    // 清理资源
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
```

```kotlin
// 提交一个UseCase
fun loadDownloadList() {
    // Unit意味不需要参数
    // onIo为Single的扩展函数，订阅在Io线程
    addDisposable(downloadListUseCase.execute(Unit).onIo())
}

fun <T> Single<T>.onIo(): Disposable {
    return subscribeOn(Schedulers.io()).subscribe()
}
```

2、暴露LiveData

```kotlin
// Success、Error、Loading
val downloadListResult = downloadListUseCase.result()
```

# View层

1、观察LiveData

```kotlin
viewModel.downloadListResult.observe(viewLifecycleOwner, Observer { result ->
    // emit函数                                                          
    result.emit({
        downloadAdapter.submitList(it.data)
        hideSwipeRefresh()
    }, {
        hideSwipeRefresh()
        view.longSnack(it.error.message.toString())
    }, {
        showSwipeRefresh()
    })
})

/**
 * Result 搭配 LiveData使用时，当配置更改或其他情况会导致LiveData重新订阅，所以定义handled变量来处理
 * 这种情况，一次性的事件，但是success的情况是有例外的，比如展示数据（在配置更改后，数据要继续显示，
 * Ui事件不需再次执行），所以应该按情况调用runIfNotHandled()
 * @param success 可能会执行多次
 * @param error 一次
 * @param loading 一次
 */
inline fun <T> Result<T>.emit(
    success: ((s: Result.Success<T>) -> Unit),
    error: ((e: Result.Error) -> Unit),
    loading: ((l: Result.Loading) -> Unit)
) {
    when (this) {
        is Result.Success -> success(this)
        is Result.Error -> runIfNotHandled { error(this) }
        is Result.Loading -> runIfNotHandled { loading(this) }
    }
}
```

2、设置点击事件

3、[Material Design 过渡动画](https://gitee.com/bytebyte6/Tv/blob/master/Material%20Design.md)

4、界面测试

- 测试按钮是否可点击，导航键是否可点击
- 测试标题是否显示或是否显示正确
- 测试收藏按钮是否显示，是否可点击，点击后预期值和实际值是否正确
- 跨应用测试，如[导入m3u文件](https://gitee.com/bytebyte6/Tv/blob/master/tv/src/androidTest/java/com/bytebyte6/view/TestImportM3uFile.kt)
- [UI测试例子](https://gitee.com/bytebyte6/Tv/tree/master/tv/src/androidTest/java/com/bytebyte6/view)


## RxJava

[RxUseCase](https://gitee.com/bytebyte6/Tv/blob/master/lib_common/src/main/java/com/bytebyte6/common/RxEx.kt)

## ExoPlayer

[基本用法](https://gitee.com/bytebyte6/Tv/blob/master/tv/src/main/java/com/bytebyte6/view/player/PlayerFragment2.kt)

## Material Design 

[过渡动画、MotionLayout](https://gitee.com/bytebyte6/Rtmp/blob/master/Material%20Design.md)

## Room

[一对多关系，多对多关系映射，fts全文检索，单元测试](https://gitee.com/bytebyte6/Tv/blob/master/Room%E5%92%8C%E5%8D%95%E5%85%83%E6%B5%8B%E8%AF%95.md)

##  网络状态监听

[NetworkHelper.kt](https://gitee.com/bytebyte6/Tv/blob/ceb1b4ae4de7a16946dc939f74cb9e8cb6573997/lib_common/src/main/java/com/bytebyte6/common/NetworkHelper.kt)

## Kotlin Gradle DSL

[依赖管理](https://gitee.com/bytebyte6/Tv/blob/master/Kotlin%20DSL%E7%AE%A1%E7%90%86%E4%BE%9D%E8%B5%96.md)

## Kotlin作用域函数

[作用域函数](https://gitee.com/bytebyte6/Tv/blob/master/%E4%BD%9C%E7%94%A8%E5%9F%9F%E5%87%BD%E6%95%B0.md)

## 自定义apk名称

[APK-Name](https://gitee.com/bytebyte6/Tv/blob/master/%E8%87%AA%E5%AE%9A%E4%B9%89Apk%E5%90%8D%E7%A7%B0.md)
