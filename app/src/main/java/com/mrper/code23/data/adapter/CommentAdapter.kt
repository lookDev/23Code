package com.mrper.code23.data.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mrper.code23.R
import com.mrper.code23.model.DemoCommentInfoEntry

/**
 * Created by admin on 2016/4/19.
 */
class CommentAdapter(val context: Context,commentlist: MutableList<DemoCommentInfoEntry>?) : BaseAdapter() {

    var commentlist: MutableList<DemoCommentInfoEntry>? = null

    init{
        this.commentlist = commentlist
    }

    override fun getCount(): Int = commentlist?.size ?: 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val item = commentlist?.get(position)
        var holder: ViewHolder?
        var itemView: View? = convertView
        if(itemView == null){
            holder = ViewHolder()
            itemView = View.inflate(context, R.layout.listitem_demo_comment,null)
            holder.imgReplyAvator = itemView.findViewById(R.id.imgReplyAvator) as ImageView
            holder.txtReplyUser = itemView.findViewById(R.id.txtReplyUser) as TextView
            holder.txtReplyTime = itemView.findViewById(R.id.txtReplyTime) as TextView
            holder.txtReplyContent = itemView.findViewById(R.id.txtReplyContent) as TextView
            itemView.tag = holder
        }else{
            holder = itemView.tag as ViewHolder
        }
        Glide.with(context).load(item!!.uface).into(holder.imgReplyAvator)
        holder.txtReplyUser?.text = item.uname
        holder.txtReplyTime?.text = item.time
        holder.txtReplyContent?.text = item.cnt
        return itemView
    }

    override fun getItem(position: Int): Any? = commentlist?.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    companion object {

        class ViewHolder(
            @JvmField var imgReplyAvator: ImageView? = null,
            @JvmField var txtReplyUser: TextView? = null,
            @JvmField var txtReplyTime: TextView? = null,
            @JvmField var txtReplyContent: TextView? = null
        )

    }

}