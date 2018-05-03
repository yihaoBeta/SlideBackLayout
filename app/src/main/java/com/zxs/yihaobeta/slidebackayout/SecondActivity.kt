package com.zxs.yihaobeta.slidebackayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.zxs.yihaobeta.slidebackayout.view.SlideBackLayout
import kotlinx.android.synthetic.main.layout_second.listview_content

/**

 * @description:

 * @author: yihaobeta

 * @version: v1.0

 * @date: 2018/4/19 下午12:51

 */
 
 
 class SecondActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_second)
    var items = mutableListOf<String>()
    for (i in 0..100) {
      items.add(i,"item:$i")
    }
    listview_content.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,items)

    title = "SecondActivity"

    var slideBackLayout = SlideBackLayout(this)
    slideBackLayout.bind(this)
    slideBackLayout.autoFinish(false)
    slideBackLayout.onSlideBackDone {
      Toast.makeText(this@SecondActivity,it,Toast.LENGTH_SHORT).show()
      finish()
    }
  }
}