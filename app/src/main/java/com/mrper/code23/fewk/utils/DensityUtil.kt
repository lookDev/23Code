package com.mrper.code23.fewk.utils

import android.content.Context
import android.util.TypedValue

/**
 * Created by Mrper on 16-1-6.
 * 单位换算辅助类
 */
object DensityUtil {

    /**  dip转为px  **/
    @JvmStatic fun dip2px(context: Context, dipValue:Float): Int
            = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dipValue,context.resources.displayMetrics).toInt()

    /**  sp转为px  **/
    @JvmStatic fun sp2px(context: Context, spValue:Float): Int
            = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spValue,context.resources.displayMetrics).toInt()

    /**  px转为dip  **/
    @JvmStatic fun px2dip(context: Context, pxValue:Float): Float = pxValue / context.resources.displayMetrics.density

    /**  px转为sp  **/
    @JvmStatic fun px2sp(context: Context, pxValue:Float): Float = pxValue / context.resources.displayMetrics.scaledDensity

}