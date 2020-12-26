package com.bytebyte6.logic

import androidx.lifecycle.LiveData
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.base.OnLoadMoreListener
import com.bytebyte6.data.IpTvRepository
import com.bytebyte6.data.LoadData
import com.bytebyte6.data.model.IpTv
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class HomeViewModel(private val repository: IpTvRepository) : BaseViewModel() {

    fun nextPage(): LiveData<List<IpTv>> {
        return repository.nextPage()
    }

    val listener = object : OnLoadMoreListener {
        override fun onLoadMore() {
            if (!isLoading()) {
                //load more

            }
        }
    }

    init {
        repository.init(object : LoadData<List<IpTv>> {
            override fun start() {
                println("start")
            }

            override fun success(data: List<IpTv>) {
                println("data.size=${data.size}")
            }

            override fun fail(error: Throwable) {
                println("error${error.message}")
            }
        })


    }
}

val viewModelModule: Module = module {
    viewModel { HomeViewModel(get(IpTvRepository::class.java)) }
}