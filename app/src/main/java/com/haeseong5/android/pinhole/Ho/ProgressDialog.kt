package com.haeseong5.android.pinhole.Ho

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.ProgressBar
import com.haeseong5.android.pinhole.R


// 참조 블로그 : https://dkfk2747.tistory.com/22

class ProgressDialog(context: Context): Dialog(context) {
//    private lateinit var tvProgress : TextView
    private lateinit var progressBar : ProgressBar

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE) //타이틀바 제거
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        progressBar = findViewById(R.id.progress_bar)
//        tvProgress = findViewById(R.id.dialog_tv_progress)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        progressBar.isIndeterminate = true
    }
}