package com.bytebyte6.rtmp

import android.os.Bundle
import com.bytebyte6.base.BaseActivity
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.logic.HomeViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {

    private lateinit var playerView: PlayerView

    private val url =
        "http://baiducdncmn-gd.inter.ptqy.gitv.tv/tslive/c16_lb_shoushiguanjun_720p_t5/c16_lb_shoushiguanjun_720p_t5.m3u8"

    private lateinit var simpleExoPlayer: SimpleExoPlayer

    val model: HomeViewModel by viewModel()

    override fun initViewModel(): BaseViewModel {
        return model
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        supportFragmentManager.beginTransaction()
//            .add(R.id.main, HomeFragment(), HomeFragment.TAG)
//            .commit()

//        playerView = findViewById(R.id.exo_play_view)
//
//        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
//
//        playerView.player = simpleExoPlayer
//
//        simpleExoPlayer.setMediaItem(MediaItem.fromUri(url))
//
//        simpleExoPlayer.prepare()
//
//        initToast()
//

//        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
//
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                println("1")
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                println("2")
//            }
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                println("3")
//                model.nextPage().observe(this@MainActivity, Observer {
//                    try {
//                        for (i in 0..1)
//                            println("one: " + it[i])
//                    } catch (e: Exception) {
//
//                    }
//                })
//            }
//        })
//

    }

}