package com.zxs.yihaobeta.slidebackayout.view

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.support.v4.widget.ViewDragHelper
import android.support.v4.widget.ViewDragHelper.Callback
import android.support.v4.widget.ViewDragHelper.create
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


/**

 * @description:支持侧滑退出的UI框架,继承自FrameLayout

 * @author: yihaobeta

 * @version: v1.0

 * @date: 2018/4/19 上午11:09

 */

class SlideBackLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

  private var mActivity: Activity? = null
  private var mSlideView: View? = null
  private lateinit var mViewDragHelper: ViewDragHelper
  //记录原始的坐标
  private val mOriginalPoint = Point()
  //记录当前滑动点的坐标
  private val mCurrentPoint = Point()
  //当前滑动方向,默认为右滑
  private var mCurOrientation = SlideOrientation.LEFT2RIGHT.value
  //滑动方向,默认为右滑
  private var mSlideOrientation = SlideOrientation.LEFT2RIGHT.value
  //是否启用自动结束当前activity的标志,true:当检测到符合条件的滑动事件时自动执行activity.finish()
  private var mAutoFinished = true
  //回调函数
  var callback: ((String) -> Unit)? = null

  //滑动方向的枚举
  enum class SlideOrientation(var value: Int) {
    //左向右滑动
    LEFT2RIGHT(ViewDragHelper.EDGE_LEFT),
    //右向左滑动
    RIGHT2LEFT(ViewDragHelper.EDGE_RIGHT);
  }

  init {
    initViewDragHelper()
  }


  /**
   * 初始化ViewDragHelper及相关滑动逻辑
   */
  private fun initViewDragHelper() {
    mViewDragHelper = create(
        this,
        1.0f,
        object : Callback() {
          override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return false
          }

          override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            mCurrentPoint.x = left
            return if (mCurOrientation != ViewDragHelper.EDGE_BOTTOM)
              left
            else
              0
          }

          override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            mCurrentPoint.y = top
            return if (mCurOrientation == ViewDragHelper.EDGE_BOTTOM)
              top
            else
              0
          }

          override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            when (mCurOrientation) {
              ViewDragHelper.EDGE_LEFT ->
                //水平向右滑动超过一半，触发结束
                if (mCurrentPoint.x > width / 2) {
                  mViewDragHelper.settleCapturedViewAt(width, mOriginalPoint.y)
                } else {
                  mViewDragHelper.settleCapturedViewAt(mOriginalPoint.x,
                      mOriginalPoint.y)
                }

              ViewDragHelper.EDGE_RIGHT ->
                //水平向左滑动超过一半，触发结束
                if (mCurrentPoint.x < -width / 2) {
                  mViewDragHelper.settleCapturedViewAt(-width, mOriginalPoint.y)
                } else {
                  mViewDragHelper.settleCapturedViewAt(mOriginalPoint.x,
                      mOriginalPoint.y)
                }
            }

            mCurrentPoint.x = 0
            mCurrentPoint.y = 0
            invalidate()
          }

          override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int,
              dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            when (mCurOrientation) {
            //处理回调及执行finish()
              ViewDragHelper.EDGE_LEFT ->
                if (left >= width) {
                  callback?.invoke("Complete-Left")
                  if (mAutoFinished) mActivity?.finish()
                }

              ViewDragHelper.EDGE_RIGHT ->
                if (left <= -width) {
                  callback?.invoke("Complete-Right")
                  if (mAutoFinished) mActivity?.finish()
                }
            }
          }

          override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            mCurOrientation = edgeFlags
            if (mSlideView == null) mSlideView = getChildAt(0)
            mViewDragHelper.captureChildView(mSlideView!!, pointerId)
          }
        })
    mViewDragHelper.setEdgeTrackingEnabled(mSlideOrientation)
  }

  override fun computeScroll() {
    if (mViewDragHelper.continueSettling(true))
      invalidate()
  }

  override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
    return mViewDragHelper.shouldInterceptTouchEvent(ev ?: return super.onInterceptTouchEvent(ev)
    )
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    event?.let {
      mViewDragHelper.processTouchEvent(event)
      return true
    }
    return false
  }


  override fun onFinishInflate() {
    super.onFinishInflate()
    if (childCount > 0) {
      mSlideView = getChildAt(0)
    }
  }

  /**
   * 绑定当前的activity
   */
  fun bind(activity: Activity) {
    this.mActivity = activity
    val a = activity.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
    val background = a.getResourceId(0, 0)
    a.recycle()
    val decorView = activity.window.decorView as ViewGroup
    val decorChild = decorView.getChildAt(0) as ViewGroup
    decorChild.setBackgroundResource(background)
    decorView.removeView(decorChild)
    addView(decorChild)
    decorView.addView(this)
  }


  /**
   * 设置监听的滑动方向
   */
  fun setSlideOrientation(orientation: SlideOrientation) {
    this.mSlideOrientation = orientation.value
    mViewDragHelper.abort()
    mViewDragHelper.setEdgeTrackingEnabled(orientation.value)
  }

  /**
   * 设置回调
   */
  fun onSlideBackDone(callback: (String) -> Unit) {
    this.callback = callback
  }

  /**
   * 设置是否自动执行finish()操作
   */
  fun autoFinish(flag: Boolean) {
    this.mAutoFinished = flag
  }
}