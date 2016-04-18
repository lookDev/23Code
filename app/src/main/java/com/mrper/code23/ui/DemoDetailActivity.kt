package com.mrper.code23.ui

import android.os.Bundle
import android.text.Html
import com.boyou.autoservice.util.CommonUtil
import com.mrper.code23.fewk.utils.ActivityUtil
import com.mrper.code23.fewk.utils.ToastUtil
import com.loopj.android.http.AsyncHttpResponseHandler
import com.mrper.code23.R
import com.mrper.code23.api.HttpManager
import com.mrper.code23.fewk.annotation.ContentView
import com.mrper.code23.fewk.ui.BaseActivity
import com.mrper.code23.model.DemoDetailInfoEntry
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_demo_detail.*

@ContentView(R.layout.activity_demo_detail)
class DemoDetailActivity : BaseActivity() {

    private lateinit var projectName: String
    private lateinit var projectUrl: String
    private lateinit var demoDetailInfo: DemoDetailInfoEntry

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
    private fun getDemoDetailInfo() {
        HttpManager.httpClient.get(context, projectUrl, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                val resultString = String(responseBody!!, charset("utf-8"))
                parseDemoDetailInfo(resultString)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                ToastUtil.showShortToast(context, "网络错误，请检查您的网络")
            }
        })
    }

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
    <a class="" href="(.+?)">Download ZIP</a>
  </footer>
</div>""",responseBody)
        if (githubMatcher.find()) {
            demoDetailInfo.projectGithub = githubMatcher.group(4)
            demoDetailInfo.projectGithubDes = githubMatcher.group(6)
            demoDetailInfo.projectGithubStar = githubMatcher.group(7)
            demoDetailInfo.projectGithubFork = githubMatcher.group(8)
            demoDetailInfo.projectDownloadUrl = githubMatcher.group(9)
        }
        txtProName.text = Html.fromHtml(projectName)
        txtProDes.text = Html.fromHtml(demoDetailInfo.projectDes)
        txtGithub.text = Html.fromHtml(demoDetailInfo.projectGithub)
        txtGithubDes.text = Html.fromHtml(demoDetailInfo.projectGithubDes)
        txtGithubStar.text = Html.fromHtml(demoDetailInfo.projectGithubStar)
        txtGithubFork.text = Html.fromHtml(demoDetailInfo.projectGithubFork)
    }

}
