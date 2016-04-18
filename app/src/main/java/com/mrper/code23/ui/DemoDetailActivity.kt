package com.mrper.code23.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mrper.code23.R
import com.mrper.code23.fewk.annotation.ContentView
import com.mrper.code23.fewk.ui.BaseActivity

@ContentView(R.layout.activity_demo_detail)
class DemoDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_demo_detail,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

}
