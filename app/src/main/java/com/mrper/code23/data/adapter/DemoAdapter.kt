package com.mrper.code23.data.adapter

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.mrper.code23.R
import com.mrper.code23.fewk.utils.DensityUtil
import com.mrper.code23.fewk.utils.DeviceUtil
import com.mrper.code23.model.DemoInfoEntry

/**
 * Created by admin on 2016/4/15.
 */
class DemoAdapter(val context: Context?,demolist: MutableList<DemoInfoEntry>?) : BaseAdapter() {

    var demolist: MutableList<DemoInfoEntry>? = null
    private var imageWidth: Int = 0

    init {
        this.demolist = demolist
        this.imageWidth = (DeviceUtil.getScreenWidth(context!!)
                - DensityUtil.dip2px(context, 10f * (COLUMN_COUNT + 1)))/COLUMN_COUNT
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val item = demolist?.get(position)
        var holder: ViewHolder?
        var itemView: View? = convertView
        if(itemView == null){
            holder = ViewHolder()
            itemView = View.inflate(context, R.layout.griditem_demo,null)
            holder.imgPicture = (itemView as View).findViewById(R.id.imgPicture) as RoundedImageView
            holder.txtProName = itemView.findViewById(R.id.txtProName) as TextView
            holder.txtPubTime = itemView.findViewById(R.id.txtPubTime) as TextView
            holder.txtDesIntro = itemView.findViewById(R.id.txtDesIntro) as TextView
            holder.txtType = itemView.findViewById(R.id.txtType) as TextView
            //数据赋值
            itemView.tag = holder
        }else{
            holder = itemView.tag as ViewHolder
        }
        //加载图片
        //设置图片布局参数
        val imgParams = holder.imgPicture?.layoutParams
        imgParams?.width = imageWidth
        imgParams?.height = imageWidth * 568 / 320
        Glide.with(context).load(item!!.pic)
                .asBitmap()
                .override(imgParams?.width?:0,imgParams?.height?:0)
                .into(holder.imgPicture)
        holder.imgPicture?.layoutParams = imgParams
        holder.txtProName?.text = Html.fromHtml(item.proName)
        holder.txtPubTime?.text = Html.fromHtml(item.pubTime)
        holder.txtDesIntro?.text = Html.fromHtml(item.desIntro)
        holder.txtType?.text = Html.fromHtml(item.typeName)
        holder.txtType?.visibility = if(TextUtils.isEmpty(item.typeName)) View.GONE else View.VISIBLE
        holder.txtPubTime?.visibility = if(TextUtils.isEmpty(item.pubTime)) View.GONE else View.VISIBLE
        return itemView
    }

    override fun getItem(position: Int): Any? = demolist?.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = demolist?.count() ?: 0

    companion object {

        @JvmStatic val COLUMN_COUNT = 2

        @JvmStatic val IMAGE_ROUND_RADIUS = 5

        class ViewHolder(
                @JvmField var imgPicture: RoundedImageView? = null,
                @JvmField var txtProName: TextView? = null,
                @JvmField var txtPubTime: TextView? = null,
                @JvmField var txtDesIntro: TextView? = null,
                @JvmField var txtType: TextView? = null
        )

    }

}