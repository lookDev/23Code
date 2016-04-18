package com.mrper.code23.model

/**
 * Created by admin on 2016/4/18.
 */
data class DemoDetailInfoEntry(
      @JvmField var projectName: String = "",
      @JvmField var projectImage: String = "",
      @JvmField var projectDes: String = "",
      @JvmField var projectGithub: String = "",
      @JvmField var projectGithubDes: String = "",
      @JvmField var projectGithubStar: String = "",
      @JvmField var projectGithubFork: String = "",
      @JvmField var projectDownloadUrl: String = ""
)