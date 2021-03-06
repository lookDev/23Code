package com.mrper.code23.ui

import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mrper.code23.R
import com.mrper.code23.api.HttpManager
import com.mrper.code23.data.adapter.CommentAdapter
import com.mrper.code23.fewk.annotation.BackAction
import com.mrper.code23.fewk.annotation.ContentView
import com.mrper.code23.fewk.dialog.DialogBigImage
import com.mrper.code23.fewk.ui.BaseActivity
import com.mrper.code23.fewk.utils.ActivityUtil
import com.mrper.code23.fewk.utils.AlertToastUtil
import com.mrper.code23.fewk.utils.ApkUtil
import com.mrper.code23.fewk.utils.CommonUtil
import com.mrper.code23.model.DemoCommentInfoEntry
import com.mrper.code23.model.DemoDetailInfoEntry
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_demo_detail.*
import org.json.JSONObject
import java.net.URLEncoder

/**  DEMO详情页面 **/
@BackAction
@ContentView(R.layout.activity_demo_detail)
class DemoDetailActivity : BaseActivity() {

    private lateinit var projectName: String
    private lateinit var projectUrl: String
    private lateinit var projectImage: String
    private lateinit var demoDetailInfo: DemoDetailInfoEntry
    private var commentlist: MutableList<DemoCommentInfoEntry> = mutableListOf()
    private var commentAdapter: CommentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取页面传递的数据
        projectName = intent?.extras?.getString("projectName") ?: ""
        projectUrl = intent?.extras?.getString("projectUrl") ?: ""
        projectImage = intent?.extras?.getString("projectImage") ?: ""
        //设置控件基本属性
        toolbar.title = Html.fromHtml(projectName)
        setToolbar(toolbar)
        demoDetailInfo = DemoDetailInfoEntry()
        demoDetailInfo.projectName = projectName
        demoDetailInfo.projectDes = intent?.extras?.getString("projectDes") ?: ""
        //设置commentAdapter
        commentAdapter = CommentAdapter(context,commentlist)
        lvComment.adapter = commentAdapter
        lvComment.emptyView = txtEmptyComment
        txtEmptyComment.text = "正在加载评论数据..."
        //设置查看图片
        btnBigImage.setOnClickListener { DialogBigImage(context,projectImage).show(supportFragmentManager,"bigImageDialog") }
        //设置跳转至github页面
        btnGithub.setOnClickListener { ActivityUtil.openBrowser(context,demoDetailInfo.projectGithub) }
        //下载文件
        btnDownload.setOnClickListener{ ApkUtil.startDownload(context,demoDetailInfo.projectDownloadUrl) }
        getDemoDetailInfo()//获取demo的详细信息
    }

    override fun onToolbarNavigationClicked() {
        super.onToolbarNavigationClicked()
        ActivityUtil.closeActivity(this)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return super.onOptionsItemSelected(item)
//    }

    /**  获取demo的详细信息  **/
    private fun getDemoDetailInfo() = HttpManager.httpClient.get(context, projectUrl, object : AsyncHttpResponseHandler() {
        override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?)
            = parseDemoDetailInfo(String(responseBody!!, charset("utf-8")))

        override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?){
            txtEmptyComment.text = "评论加载失败"
            AlertToastUtil.showToast(context, "网络错误，请检查您的网络")
        }
    })

    /**  获取demo评论数据 **/
    private fun getDemoCommentInfo(shortUrl: String,projectUrl: String) = HttpManager.httpClient.get(context,
            HttpManager.CommentURL.replace("{shortUrl}",URLEncoder.encode(shortUrl.replace("http://",""),"utf-8"))
                    .replace("{projectUrl}",URLEncoder.encode(projectUrl.replace("http://",""),"utf-8")),
            object : AsyncHttpResponseHandler() {
        override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?)
                = parseDemoCommentInfo(String(responseBody!!, charset("utf-8")))

        override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?){
            txtEmptyComment.text = "评论加载失败"
            AlertToastUtil.showToast(context, "网络错误，请检查您的网络")
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
        //匹配Github说明
        val githubMatcher = CommonUtil.regexMatcher(
                """<div class="reposidget">[^<]+<header class="fontello">[^<]+<span class="fontello info"><a href="(.+?)" target="_blank">[^<]+</a></span>[^<]+<h2>"""
                        + """[^<]+<a href="(.+?)">([^<]+)</a>[^<]+<span>[^<]+</span>[^<]+<a href="(.+?)"><strong>([^<]+)</strong></a>[^<]+</h2>[^<]+</header>[^<]+<section>"""
                        + """[^<]+<p[^>]+>([^<]+)?</p>[^<]+<p[^>]+><a[^>]+><strong>[^<]+</strong></a></p>[^<]+</section>[^<]+<footer>[^<]+<span class="fontello star">([^<]+)"""
                        + """</span><span class="fontello fork">([^<]+)</span>[^<]+<a.+?href="(.+?)">Download ZIP</a>[^<]+</footer>[^<]+</div>""",responseBody)
        if (githubMatcher.find()) {
            with(demoDetailInfo) {
                projectGithub = githubMatcher.group(4)
                projectGithubDes = try { githubMatcher.group(6) } catch(e: Exception) { "暂无介绍" }
                projectGithubStar = githubMatcher.group(7)
                projectGithubFork = githubMatcher.group(8)
                projectDownloadUrl = githubMatcher.group(9)
                txtGithub.text = Html.fromHtml(projectGithub)
                txtGithubDes.text = Html.fromHtml(projectGithubDes)
                txtGithubStar.text = Html.fromHtml(projectGithubStar)
                txtGithubFork.text = Html.fromHtml(projectGithubFork)
            }
            githubContainer.visibility = View.VISIBLE
            downloadContainer.visibility = View.VISIBLE
        }
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
