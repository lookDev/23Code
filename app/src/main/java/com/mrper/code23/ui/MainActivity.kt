package com.mrper.code23.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mrper.code23.R
import com.mrper.code23.api.HttpManager
import com.mrper.code23.data.adapter.DemoAdapter
import com.mrper.code23.fewk.annotation.ContentView
import com.mrper.code23.fewk.ui.BaseActivity
import com.mrper.code23.model.DemoInfoEntry
import com.mrper.code23.model.TypeInfoEntry
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern

@ContentView(R.layout.activity_main)
class MainActivity : BaseActivity() {

    private var demolist: MutableList<DemoInfoEntry> = mutableListOf()
    private var demoAdapter: DemoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置标题部分
        toolbar.title = "23Code"
        toolbar.setTitleTextColor(Color.WHITE)
        setToolbar(toolbar)
        //设置基本控件
        slideMenu.sliderFadeColor = Color.TRANSPARENT
        demoAdapter = DemoAdapter(this@MainActivity,demolist)
        lvDemo.setAdapter(demoAdapter)
        //获取页面数据
        HttpManager.httpClient.get(this,HttpManager.BaseURL,object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                var responseResult = String(responseBody!!,charset("utf-8"))
                parseDemoTypes(responseResult)//解析类型列表
                parseDemoList(responseResult)//解析案例列表数据
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }
        })
    }

    /**
     * 获取案例列表数据
     * @param typeName 类型名称
     * @param pagesize 页码
     */
    private fun getDemoList(typeName: String,pagesize: Int) {
        HttpManager.httpClient.get(this,HttpManager.getAbsoluteURL(typeName + "/page/$pagesize"),object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                parseDemoList(String(responseBody!!,charset("utf-8")))
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }
        })
    }

    /**
     * 解析案例类型
     * @param responseBody 结果数据
     */
    private fun parseDemoTypes(responseBody: String?){
        val htmlMatch = Pattern.compile("<li class=\"cat-item cat-item-\\d+\"><a href=\"http://www.23code.com/(.+?)/\"[^>]*>(.+?)</a>")
        val matcher = htmlMatch.matcher(responseBody)
        var typelist: MutableList<TypeInfoEntry> = mutableListOf()
        while(matcher.find()) typelist.add(TypeInfoEntry(matcher.group(2),matcher.group(1)))
        lvType.adapter = ArrayAdapter(this@MainActivity,android.R.layout.simple_list_item_1,typelist)
    }

    /**
     * 解析案例列表数据
     * @param responseBody 数据题
     * @param isClearData 是否情况数据
     */
    private fun parseDemoList(responseBody: String?,isClearData: Boolean = false){
        val htmlPattern = Pattern.compile("<article\\s+id=\"entry[-]\\d+\"[^>]+>([\\s\\S]*?)</article>")
        var matcher = htmlPattern.matcher(responseBody)
        var demolist: MutableList<DemoInfoEntry> = mutableListOf()
        while(matcher.find()){
            val demoItem = DemoInfoEntry()
            var itemContent = matcher.group(1)
            //匹配图标
            var picPattern = Pattern.compile("<img.+?src=\"(.+?)\" class=\"attachment-thumbnail-wzh size-thumbnail-wzh wp-post-image\"[^>]+>")
            var picMatcher = picPattern.matcher(itemContent)
            if(picMatcher.find())
                demoItem.pic = picMatcher.group(1) //图片
            //匹配项目名称
            var namePattern = Pattern.compile("<a href=\"(.+?)\" rel=\"bookmark\" [^>]+>([^>]+)</a>")
            var nameMatcher = namePattern.matcher(itemContent)
            if(nameMatcher.find()) {
                demoItem.proUrl = nameMatcher.group(1)
                demoItem.proName = nameMatcher.group(2)
            }
            //匹配发布时间
            var timePattern = Pattern.compile("<a href=\".+?\" title=\"由23Code发布\" rel=\"author\">23Code</a>([^<]+)</p>")
            var timeMatcher = timePattern.matcher(itemContent)
            if(timeMatcher.find())
                demoItem.pubTime = timeMatcher.group(1).replace("on","")
                        .replace("年",".").replace("月",".").replace("日","")
            //匹配说明
            var desPattern = Pattern.compile("<div class=\"text\">[^<]+<p>([^<]+)</p>[^<]+</div>")
            var desMatcher = desPattern.matcher(itemContent)
            if(desMatcher.find())
                demoItem.desIntro = desMatcher.group(1)
            //匹配分类
            var typePattern = Pattern.compile("<li class=\"categories\"><i[^>]+></i><a[^>]+>([^<]+)</a></li>")
            var typeMatcher = typePattern.matcher(itemContent)
            if(typeMatcher.find())
                demoItem.typeName = typeMatcher.group(1)
            demolist.add(demoItem)
        }
        if(isClearData) this@MainActivity.demolist.clear()
        this@MainActivity.demolist.addAll(demolist)
        demoAdapter?.notifyDataSetChanged()
    }

    //<article\s+id="entry-\d+"[^>]+>([\s\S]*?)</article>  匹配某一列
    //<img.+?src="(.+?)" class="attachment-thumbnail-wzh size-thumbnail-wzh wp-post-image"[^>]+> 匹配ICON
    //<a href="(.+?)" rel="bookmark" [^>]+>([^>]+)</a>  匹配项目名称
    //<a href=".+?" title="由23Code发布" rel="author">23Code</a>([^<]+)</p>  匹配发布时间
    //<div class="text">[^<]+<p>([^<]+)</p>[^<]+</div>  匹配说明
    //<li class="categories"><i[^>]+></i><a[^>]+>([^<]+)</a></li> 匹配类型


}
