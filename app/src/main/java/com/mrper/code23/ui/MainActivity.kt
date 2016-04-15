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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.title = "23Code"
        toolbar.setTitleTextColor(Color.WHITE)
        setToolbar(toolbar)
        slideMenu.setBackgroundResource(R.mipmap.ic_main_bg)
        HttpManager.httpClient.get(this,HttpManager.BaseURL,object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                //解析类型列表
                parseDemoTypes(responseBody)
                //解析案例列表数据
                parseDemoList(responseBody)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }
        })

    }


    private fun getDemoList(typeName: String,pagesize: Int) {
        HttpManager.httpClient.get(this,HttpManager.getAbsoluteURL(typeName + "/page/$pagesize"),object: AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                parseDemoList(responseBody)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {

            }
        })
    }

    private fun parseDemoTypes(responseBody: ByteArray?){
        val htmlMatch = Pattern.compile("<li class=\"cat-item cat-item-\\d+\"><a href=\"http://www.23code.com/(.+?)/\"[^>]*>(.+?)</a>")
        val matcher = htmlMatch.matcher(String(responseBody!!,charset("utf-8")))
        var typelist: MutableList<TypeInfoEntry> = mutableListOf()
        while(matcher.find()) typelist.add(TypeInfoEntry(matcher.group(2),matcher.group(1)))
        lvType.adapter = ArrayAdapter(this@MainActivity,android.R.layout.simple_list_item_1,typelist)
    }

    private fun parseDemoList(responseBody: ByteArray?){
        val htmlPattern = Pattern.compile("<article\\s+id=\"entry[-]\\d+\"[^>]+>([\\s\\S]*?)</article>")
        var matcher = htmlPattern.matcher(String(responseBody!!,charset("utf-8")))
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
        lvDemo.setAdapter(DemoAdapter(this@MainActivity,demolist))
    }

    //<article\s+id="entry-\d+"[^>]+>([\s\S]*?)</article>  匹配某一列
    //<img.+?src="(.+?)" class="attachment-thumbnail-wzh size-thumbnail-wzh wp-post-image"[^>]+> 匹配ICON
    //<a href="(.+?)" rel="bookmark" [^>]+>([^>]+)</a>  匹配项目名称
    //<a href=".+?" title="由23Code发布" rel="author">23Code</a>([^<]+)</p>  匹配发布时间
    //<div class="text">[^<]+<p>([^<]+)</p>[^<]+</div>  匹配说明
    //<li class="categories"><i[^>]+></i><a[^>]+>([^<]+)</a></li> 匹配类型


}
