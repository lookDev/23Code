package com.mrper.code23.api

import com.loopj.android.http.AsyncHttpClient

/**
 * Created by admin on 2016/4/15.
 */
class HttpManager {

    companion object {

        @JvmField val BaseURL = "http://www.23code.com/"

        @JvmField val httpClient = AsyncHttpClient()

        @JvmStatic fun getAbsoluteURL(relativeUrl: String) = BaseURL + relativeUrl

    }

}