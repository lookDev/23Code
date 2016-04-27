package com.mrper.crashcatcher.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem

import com.mrper.crashcatcher.R
import com.mrper.crashcatcher.model.ExceptionInfoEntry
import com.mrper.crashcatcher.util.common.CatcherShareUtil
import kotlinx.android.synthetic.main.activity_exception_detail.*

class ExceptionDetailActivity : AppCompatActivity() {

    private var exception: ExceptionInfoEntry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exception_detail)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        txtException.movementMethod = ScrollingMovementMethod()
        exception = intent?.getParcelableExtra<ExceptionInfoEntry>("exception")
        txtAppName.text = exception?.appName
        txtAppVersion.text = exception?.appVersion
        txtStatus.text = if(exception?.appIsDeal ?: false) "已处理" else "未处理"
        txtCreateTime.text = exception?.appExceptionCreateTime
        txtPhoneBrand.text = exception?.phoneBrand + " => " + exception?.phoneModel
        txtAndroid.text = exception?.phoneAndridVersion
        txtException.text = exception?.appException
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_exception_detail,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId){
        R.id.menu_action_share -> {
            CatcherShareUtil.shareText(this@ExceptionDetailActivity,exception?.appException)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
