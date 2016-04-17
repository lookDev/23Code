package com.mrper.code23.data.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.boyou.autoservice.util.sysutil.DensityUtil
import com.boyou.autoservice.util.sysutil.DeviceUtil
import com.bumptech.glide.Glide
import com.mrper.code23.R
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
                - DensityUtil.dip2px(context,35f + 10f * (COLUMN_COUNT - 1)))/COLUMN_COUNT
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val item = demolist?.get(position)
        var holder: ViewHolder? = null
        var itemView: View? = convertView
        if(itemView == null){
            holder = ViewHolder()
            itemView = View.inflate(context, R.layout.griditem_demo,null)
            holder.imgPicture = (itemView as View).findViewById(R.id.imgPicture) as ImageView
            holder.txtProName = itemView.findViewById(R.id.txtProName) as TextView
            holder.txtPubTime = itemView.findViewById(R.id.txtPubTime) as TextView
            holder.txtDesIntro = itemView.findViewById(R.id.txtDesIntro) as TextView
            //数据赋值
            itemView.tag = holder
        }else{
            holder = itemView.tag as ViewHolder
        }
        Glide.with(context).load(item!!.pic).into(holder.imgPicture)
        //设置图片布局参数
        val imgParams = holder.imgPicture?.layoutParams
        imgParams?.width = imageWidth
        imgParams?.height = imageWidth * 568 / 320
        holder.imgPicture?.layoutParams = imgParams
        holder.txtProName?.text = item.proName
        holder.txtPubTime?.text = item.pubTime
        holder.txtDesIntro?.text = item.desIntro
        return itemView
    }

    override fun getItem(position: Int): Any? = demolist?.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = demolist?.count() ?: 0

    companion object {

        @JvmStatic val COLUMN_COUNT = 2

        class ViewHolder(
                var imgPicture: ImageView? = null,
                var txtProName: TextView? = null,
                var txtPubTime: TextView? = null,
                var txtDesIntro: TextView? = null
        )

    }

}