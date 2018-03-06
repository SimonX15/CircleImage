package com.simon.app.lib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView

/**
 * desc: 圆形图片，会根据控件宽高对图片进行缩放
 *
 * auther: xw
 *
 * date: 2018/3/6
 *
 * @auther: xw
 */
class CircleImageView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {

    /** 画笔 */
    private var mPaint: Paint? = null

    /** 目标图片 */
    private var dstBitmap: Bitmap? = null

    /** 形状 */
    private var circleBitmap: Bitmap? = null

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.apply {
            isDither = true //设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
            isFilterBitmap = true //加快显示速度，本设置项依赖于dither和xfermode的设置
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //如果控件宽高不一致，取最小值
        val width = Math.min(measuredWidth, measuredHeight)
        setMeasuredDimension(width, width)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (drawable == null) {
            return
        }

        if (dstBitmap == null) {
            dstBitmap = createDstBitmap()
        }

        if (circleBitmap == null) {
            circleBitmap = createCircleBG(width)
        }

        canvas?.apply {
            val layer = saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)//新建layer

            drawBitmap(dstBitmap, 0f, 0f, mPaint)
            mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            drawBitmap(circleBitmap, 0f, 0f, mPaint)

            mPaint!!.xfermode = null
            //layer退栈
            restoreToCount(layer)
        }
    }

    /** 绘制目标图片 */
    private fun createDstBitmap(): Bitmap? {
        val dWidth = drawable.intrinsicWidth
        val dHeight = drawable.intrinsicHeight

        //缩放
        val scale = Math.max(width * 1f / dWidth, height * 1f / dHeight)
        val scaleWidth = (dWidth * scale).toInt()
        val scaleHeight = (dHeight * scale).toInt()

        drawable.setBounds(0, 0, scaleWidth, scaleHeight)

        val dstBitmap = Bitmap.createBitmap(scaleWidth, scaleHeight, Bitmap.Config.ARGB_8888)
        val dstCanvas = Canvas(dstBitmap)
        drawable.draw(dstCanvas)

        return dstBitmap
    }

    /** 绘制源图片，圆形 */
    private fun createCircleBG(width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(width / 2f, width / 2f, width / 2f, mPaint)
        return bitmap
    }
}