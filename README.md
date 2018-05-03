# SlideBackLayout
kotlin写的一个activity左滑退出的小框架
----

## 使用方法

* 在要使用滑动退出的activity中添加如下代码
```java
    var slideBackLayout = SlideBackLayout(this)
    slideBackLayout.bind(this)
```

* 更多用法
  * 设置滑动方向 
  `slideBackLayout.setSlideOrientation(RIGHT2LEFT)`
  * 设置滑动触发后是否自动finish() `slideBackLayout.autoFinish(false)`
  * 设置滑动触发后的监听操作
  ```java
  slideBackLayout.onSlideBackDone {
      Toast.makeText(this@SecondActivity,it,Toast.LENGTH_SHORT).show()
      finish()
    }
  ```
