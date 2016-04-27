package com.mrper.crashcatcher.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.j256.ormlite.dao.Dao
import com.mrper.crashcatcher.R
import com.mrper.crashcatcher.model.ExceptionInfoEntry
import com.mrper.crashcatcher.util.database.SQLiteHelper
import kotlinx.android.synthetic.main.activity_exception.*

class ExceptionActivity : AppCompatActivity() {

    private var exceptionAdapter: ExceptionAdapter? = null
    private var dao: Dao<ExceptionInfoEntry,Int>? = null
    private var curPageIndex = -1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exception)
        exceptionAdapter = ExceptionAdapter(this)
        lvException.emptyView = txtEmpty
        lvException.adapter = exceptionAdapter
        lvException.setOnScrollListener(object : AbsListView.OnScrollListener{
            private var state: Int? = null
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if(state!=null){
                    if(state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && firstVisibleItem + visibleItemCount == totalItemCount - 1){
                        queryExceptions(curPageIndex + 1)
                    }
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                state = scrollState
            }
        })
        lvException.setOnItemClickListener {
            adapterView, view, i, l -> run {
                val item = exceptionAdapter?.getItem(i) as ExceptionInfoEntry
                val intent = Intent(this@ExceptionActivity,ExceptionDetailActivity::class.java)
                intent.putExtra("exception",item)
                startActivity(intent)
            }
        }
        queryExceptions(curPageIndex + 1)
    }

    private fun queryExceptions(page: Int){
        if(!isLoading) {
            isLoading = true
            if (dao == null)
                dao = SQLiteHelper.getInstance(this)?.getDao(ExceptionInfoEntry::class.java)
            val dataResult = dao?.queryBuilder()?.orderBy("appExceptionCreateTime", false)?.offset(page * 15L)?.limit(15L)?.query()
            if (dataResult != null && dataResult.size > 0) {
                exceptionAdapter?.addItems(dataResult)
                curPageIndex = page
            } else {
                Toast.makeText(this, "已是最后一页", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
        }
    }


    companion object {

        class ExceptionAdapter(val context: Context,data: MutableList<ExceptionInfoEntry>? = null) : BaseAdapter() {

            private var data: MutableList<ExceptionInfoEntry>? = null
            private var inflater: LayoutInflater? = null

            init{
                this.data = data ?: mutableListOf()
                inflater = LayoutInflater.from(context)
            }

            @Synchronized fun addItems(vararg items: ExceptionInfoEntry){
                if(items.size>0){
                    data?.addAll(items)
                    notifyDataSetChanged()
                }
            }

            @Synchronized fun addItems(items: List<ExceptionInfoEntry>?){
                if(items != null && items.size>0){
                    data?.addAll(items)
                    notifyDataSetChanged()
                }
            }

            @Synchronized fun clear(){
                data?.clear()
                notifyDataSetChanged()
            }

            override fun getCount(): Int = data?.size ?: 0

            override fun getItem(position: Int): Any? = data?.get(position)

            override fun getItemId(position: Int): Long = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
                val item: ExceptionInfoEntry? = data?.get(position)
                val holder: ViewHolder?
                var itemView: View? = convertView
                if(itemView == null){
                    itemView = inflater?.inflate(R.layout.listitem_exception,null,false)
                    holder = ViewHolder(
                            txtAppName = itemView?.findViewById(R.id.txtAppName) as? TextView?,
                            txtAppVersion = itemView?.findViewById(R.id.txtVersionName) as? TextView?,
                            txtDescription = itemView?.findViewById(R.id.txtException) as? TextView?,
                            txtPhoneBrand = itemView?.findViewById(R.id.txtPhoneBrand) as? TextView?,
                            txtCreateTime = itemView?.findViewById(R.id.txtCreateTime) as? TextView?
                    )
                    itemView?.tag = holder
                }else{
                    holder = itemView.tag as? ViewHolder
                }
                holder?.txtAppName?.text = item?.appName
                holder?.txtAppVersion?.text = "版本：${item?.appVersion}"
                holder?.txtDescription?.text = item?.appException
                holder?.txtPhoneBrand?.text = "品牌：${item?.phoneBrand} => ${item?.phoneModel}"
                holder?.txtCreateTime?.text = "时间：${item?.appExceptionCreateTime}"
                return itemView
            }

            companion object {

                data class ViewHolder(
                        var txtAppName: TextView? = null,
                        var txtAppVersion: TextView? = null,
                        var txtDescription: TextView? = null,
                        var txtPhoneBrand: TextView? = null,
                        var txtCreateTime: TextView? = null
                )

            }

        }

    }

}
