package com.mrper.code23.fewk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Mrper on 15-12-27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BackAction {
        /**  进入页面时的动画资源ID  默认为：slide_in_right  */
        int enterAnimationId() default android.R.anim.slide_in_left;
        /**  退出页面时的动画资源ID  默认为：slide_out_left  */
        int exitAnimationId() default android.R.anim.slide_out_right;
}
