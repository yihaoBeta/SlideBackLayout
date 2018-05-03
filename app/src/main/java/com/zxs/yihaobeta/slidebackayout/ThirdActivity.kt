package com.zxs.yihaobeta.slidebackayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zxs.yihaobeta.slidebackayout.view.SlideBackLayout
import com.zxs.yihaobeta.slidebackayout.view.SlideBackLayout.SlideOrientation.RIGHT2LEFT

/**

 * @description:

 * @author: yihaobeta

 * @version: v1.0

 * @date: 2018/4/19 下午2:44

 */
 
 
class ThirdActivity : AppCompatActivity(){
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_third)
    var slideBackLayout = SlideBackLayout(this)
    slideBackLayout.bind(this)
    title = "ThirdActivity"
    slideBackLayout.setSlideOrientation(RIGHT2LEFT)
  }
}