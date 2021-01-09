package com.bytebyte6.base_ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ExampleActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

//        if (savedInstanceState==null){
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.main,ListFragment())
//                .commit()
//        }
    }
}