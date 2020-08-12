package com.haeseong5.android.pinhole

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    fun printToast(message: String){
         Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}