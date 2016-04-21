package com.mrper.code23.api

import com.loopj.android.http.AsyncHttpClient

/**
 * Created by admin on 2016/4/15.
 * Http访问管理
 */
class HttpManager {

    companion object {

        /** 网络访问HttpClient **/
        @JvmField val httpClient = AsyncHttpClient()

        /** 主域名  **/
        @JvmField val BaseURL = "http://www.23code.com/"

        /** 搜索地址 **/
        @JvmField val SearchURL = "$BaseURL{page}?s={keyword}"

        /** 留言地址 **/
        @JvmField val CommentURL = "http://api.v2.uyan.cc/v4/comment/?su={shortUrl}&url={projectUrl}"

        /** 访问地址转化  **/
        @JvmStatic fun getAbsoluteURL(relativeUrl: String) = BaseURL + relativeUrl

    }

}