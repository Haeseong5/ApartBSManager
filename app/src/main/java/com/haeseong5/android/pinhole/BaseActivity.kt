package com.haeseong5.android.pinhole

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(){
    private var progressDialog : com.haeseong5.android.pinhole.dong.ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun printToast(message: String){
         Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun showProgressDialog(){
        if(progressDialog == null){
            progressDialog = com.haeseong5.android.pinhole.dong.ProgressDialog(this)
        }

        progressDialog!!.show()
    }
    fun dismissProgressDialog(){
        if(progressDialog != null && progressDialog!!.isShowing){
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.haeseong5.android.pinhole.R.menu.menu_items,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
            com.haeseong5.android.pinhole.R.id.action_work ->{
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}