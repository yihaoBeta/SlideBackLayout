package com.zxs.yihaobeta.slidebackayout

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.button
import kotlinx.android.synthetic.main.activity_main.button2

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    title = "MainActivity"
    button.setOnClickListener{
      startActivity(Intent(this@MainActivity,SecondActivity::class.java))
    }

    button2.setOnClickListener{
      startActivity(Intent(this@MainActivity,ThirdActivity::class.java))
    }
  }
}
