package com.bytebyte6.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bytebyte6.base.Result
import com.bytebyte6.data.dao.TvDao
import com.bytebyte6.data.entity.Tv
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule

@RunWith(AndroidJUnit4::class)
class TestPagingHelper : KoinTest {

    private lateinit var db: AppDatabase
    private lateinit var tvDao: TvDao
    private lateinit var context: Context

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(dataModule)
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        tvDao = db.tvDao()
        tvDao.insert(tvList)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testPaging() {
        val pagingHelper = object : com.bytebyte6.base.PagingHelper<Tv>() {
            override fun count(): Int {
                return tvDao.getCount()
            }

            override fun paging(offset: Int, pageSize:Int): List<Tv> {
                return tvDao.paging(offset)
            }
        }

        //记下 成功 错误 Loading 次数
        var successCount = 0
        var errorCount = 0
        var loadingCount = 0

        val observer = Observer<Result<List<Tv>>> {
            when (it) {
                is Result.Success -> {
                    successCount = successCount.plus(1)
                }
                is Result.Error -> {
                    errorCount = errorCount.plus(1)
                }
                is Result.Loading -> {
                    loadingCount = loadingCount.plus(1)
                }
            }
        }

        pagingHelper.result().observeForever(observer)

        //总页数
        val pageCount = tvDao.getCount()
            .div(PAGE_SIZE)
            .plus(
                if (tvDao.getCount() % PAGE_SIZE == 0)
                    0
                else
                    1
            )

        //数据刚好加载完成
        for (i in 0 until pageCount) {
            pagingHelper.loadResult().subscribe()
        }
        assert(pagingHelper.getPage()==pageCount)

        //判断end标志是否正确
        val peekContent0 = pagingHelper.result().value
        assert(peekContent0 is Result.Success && peekContent0.end)

        assert(successCount == pageCount)
        assert(loadingCount == pageCount)
        assert(loadingCount == successCount)
        assert(errorCount == 0)
        assert(pagingHelper.getCurrentSize() == FAKE_SIZE)
    }
}