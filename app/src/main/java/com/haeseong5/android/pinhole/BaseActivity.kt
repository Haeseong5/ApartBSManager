package com.haeseong5.android.pinhole

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.haeseong5.android.pinhole.Ho.HoActivity
import com.haeseong5.android.pinhole.work.WorkActivity


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(){
    private var progressDialog : com.haeseong5.android.pinhole.Ho.ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun printToast(message: String){
         Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun showProgressDialog(){
        if(progressDialog == null){
            progressDialog = com.haeseong5.android.pinhole.Ho.ProgressDialog(this)
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
                val intent = Intent(this, WorkActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}