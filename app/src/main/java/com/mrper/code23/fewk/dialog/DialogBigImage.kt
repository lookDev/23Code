package com.mrper.code23.fewk.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mrper.code23.R
import com.mrper.code23.fewk.utils.DensityUtil
import com.mrper.code23.fewk.utils.DeviceUtil

/**
 * Created by admin on 2016/4/20.
 * 查看大图的Dialog对话框
 */
class DialogBigImage(val ctx: Context,val imgUrl: String) : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = View.inflate(ctx, R.layout.dialog_big_image,null)
        val imgView = dialogView.findViewById(R.id.imgPicture) as ImageView
        val imgWidth = DeviceUtil.getScreenWidth(ctx) - DensityUtil.dip2px(ctx,30f)
        val imgHeight = imgWidth * 568 / 320
        val imgParams = imgView.layoutParams
        imgParams.width = imgWidth
        imgParams.height = imgHeight
        imgView.layoutParams = imgParams
        Glide.with(this).load(imgUrl).into(imgView)
        val dialog = AlertDialog.Builder(ctx)
//                .setTitle("查看大图")
                .setView(dialogView)
                .setPositiveButton("确认",{ dialoginterface,which -> dialoginterface.dismiss() })
                .create()
        return dialog
    }
}