package com.mrper.code23.ui

import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mrper.code23.R
import com.mrper.code23.api.HttpManager
import com.mrper.code23.data.adapter.CommentAdapter
import com.mrper.code23.fewk.annotation.ContentView
import com.mrper.code23.fewk.ui.BaseActivity
import com.mrper.code23.fewk.utils.ActivityUtil
import com.mrper.code23.fewk.utils.CommonUtil
import com.mrper.code23.fewk.utils.ToastUtil
import com.mrper.code23.model.DemoCommentInfoEntry
import com.mrper.code23.model.DemoDetailInfoEntry
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_demo_detail.*
import org.json.JSONObject
import java.net.URLEncoder

@ContentView(R.layout.activity_demo_detail)
class DemoDetailActivity : BaseActivity() {

    private lateinit var projectName: String
    private lateinit var projectUrl: String
    private lateinit var demoDetailInfo: DemoDetailInfoEntry
    private var commentlist: MutableList<DemoCommentInfoEntry> = mutableListOf()
    private var commentAdapter: CommentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取页面传递的数据
        projectName = intent?.extras?.getString("projectName") ?: ""
        projectUrl = intent?.extras?.getString("projectUrl") ?: ""
        //设置控件基本属性
        toolbar.title = projectName
        setToolbar(toolbar)
        demoDetailInfo = DemoDetailInfoEntry()
        demoDetailInfo.projectName = projectName
        demoDetailInfo.projectDes = intent?.extras?.getString("projectDes") ?: ""
        //设置commentAdapter
        commentAdapter = CommentAdapter(context,commentlist)
        lvComment.adapter = commentAdapter
        lvComment.emptyView = txtEmptyComment
        txtEmptyComment.text = "正在加载评论数据..."
        getDemoDetailInfo()//获取demo的详细信息
    }

    override fun onToolbarNavigationClicked() {
        super.onToolbarNavigationClicked()
        ActivityUtil.closeActivity(this)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_demo_detail, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return super.onOptionsItemSelected(item)
//    }

    /**  获取demo的详细信息  **/
    private fun getDemoDetailInfo() = HttpManager.httpClient.get(context, projectUrl, object : AsyncHttpResponseHandler() {
        override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
            val resultString = String(responseBody!!, charset("utf-8"))
            parseDemoDetailInfo(resultString)
        }

        override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
            ToastUtil.showShortToast(context, "网络错误，请检查您的网络")
        }
    })

    /**  获取demo评论数据 **/
    private fun getDemoCommentInfo(shortUrl: String,projectUrl: String) = HttpManager.httpClient.get(context,
            "http://api.v2.uyan.cc/v4/comment/?su=${URLEncoder.encode(shortUrl.replace("http://",""),"utf-8")}&url=${URLEncoder.encode(projectUrl.replace("http://",""),"utf-8")}",
            object : AsyncHttpResponseHandler() {
        override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
            val resultString = String(responseBody!!, charset("utf-8"))
            parseDemoCommentInfo(resultString)
        }

        override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
            ToastUtil.showShortToast(context, "网络错误，请检查您的网络")
        }
    })

    /**
     * 解析DEMO的详情数据
     * @param responseBody
     */
    private fun parseDemoDetailInfo(responseBody: String) {
        //匹配图片
        val imgMatcher = CommonUtil.regexMatcher("<img alt=\".+?23Code\" class=\"aligncenter size-full wp-image-\\d+\" src=\"(.+?)\"[^>]+>", responseBody)
        if (imgMatcher.find()) demoDetailInfo.projectImage = imgMatcher.group(1)
        //匹配Github说明
        val githubMatcher = CommonUtil.regexMatcher(
                """<div class="reposidget">
  <header class="fontello">
    <span class="fontello info"><a href="(.+?)" target="_blank">[^<]+</a></span>
    <h2>
      <a href="(.+?)">([^<]+)</a>
      <span> / </span>
      <a href="(.+?)"><strong>([^<]+)</strong></a>
    </h2>
  </header>
  <section>
    <p class="">([^<]+)</p>
    <p[^>]+><a[^>]+><strong>[^<]+</strong></a></p>
  </section>
  <footer>
    <span class="fontello star">([^<]+)</span><span class="fontello fork">([^<]+)</span>
    <a.+?href="(.+?)">Download ZIP</a>
  </footer>
</div>""",responseBody)
        if (githubMatcher.find()) {
            demoDetailInfo.projectGithub = githubMatcher.group(4)
            demoDetailInfo.projectGithubDes = githubMatcher.group(6)
            demoDetailInfo.projectGithubStar = githubMatcher.group(7)
            demoDetailInfo.projectGithubFork = githubMatcher.group(8)
            demoDetailInfo.projectDownloadUrl = githubMatcher.group(9)
        }
        //匹配发布时间
        val pubTimeMatcher = CommonUtil.regexMatcher("<a.+?rel=\"author\">23Code</a>\\s*on\\s*([^<]+)</p>",responseBody)
        if(pubTimeMatcher.find())
            demoDetailInfo.projectPubTime = pubTimeMatcher.group(1)
        //匹配短链接
        val shortUrlMatcher = CommonUtil.regexMatcher("<link rel='shortlink' href='(.+?)'[^>]+>",responseBody)
        if(shortUrlMatcher.find()) {
            demoDetailInfo.shortUrlLink = shortUrlMatcher.group(1)
            getDemoCommentInfo(demoDetailInfo.shortUrlLink,projectUrl) //获取评论数据
        }
        //控件赋值
        txtProName.text = Html.fromHtml(projectName)
        txtProDes.text = Html.fromHtml(demoDetailInfo.projectDes)
        txtProTime.text = Html.fromHtml(if(!TextUtils.isEmpty(demoDetailInfo.projectPubTime)) "${demoDetailInfo.projectPubTime}发布" else "暂无信息" )
        txtGithub.text = Html.fromHtml(demoDetailInfo.projectGithub)
        txtGithubDes.text = Html.fromHtml(demoDetailInfo.projectGithubDes)
        txtGithubStar.text = Html.fromHtml(demoDetailInfo.projectGithubStar)
        txtGithubFork.text = Html.fromHtml(demoDetailInfo.projectGithubFork)
    }

    /**  解析案例评论数据 **/
    private fun parseDemoCommentInfo(responseBody: String){
        //匹配评论数据
        val commentMatcher = CommonUtil.regexMatcher("UYAN_RENDER[.]comment\\((.+?)\\);",responseBody)
        val commentlist:MutableList<DemoCommentInfoEntry> = mutableListOf()
        if(commentMatcher.find()){
            val jsonObj = JSONObject(commentMatcher.group(1))
            val commentItems = jsonObj.getJSONArray("data")
            val comments: MutableList<DemoCommentInfoEntry> = Gson().fromJson<MutableList<DemoCommentInfoEntry>>(commentItems.toString(),
                    object: TypeToken<MutableList<DemoCommentInfoEntry>>(){}.type)
            commentlist.addAll(comments)
        }
        if(commentlist.size > 0){
            this@DemoDetailActivity.commentlist.addAll(commentlist)
            commentAdapter?.notifyDataSetChanged()
            txtEmptyComment.text = ""
        }else{
            txtEmptyComment.text = "暂无评论数据"
        }
    }

}
