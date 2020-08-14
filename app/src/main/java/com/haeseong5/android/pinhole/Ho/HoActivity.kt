package com.haeseong5.android.pinhole.Ho

import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haeseong5.android.pinhole.BaseActivity
import com.haeseong5.android.pinhole.DBHelper
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_BAD
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_DONE
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_ETC
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_INCOPLETE
import com.haeseong5.android.pinhole.R
import kotlinx.android.synthetic.main.activity_ho.*
import kotlinx.android.synthetic.main.toolbar.*

class HoActivity : BaseActivity() {
    private var itemList = arrayListOf<Ho>()
    private val db: DBHelper = DBHelper(this)
    private var apart_name :String? = null
    private var complex_id :Int = 0
    private var complex_name :String? = null
    private var complex_line :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ho)
        if (intent.hasExtra("complex_line") && intent.hasExtra("apart_name")
            && intent.hasExtra("complex_id") && intent.hasExtra("complex_name")) {
            apart_name = intent.getStringExtra("apart_name")
            complex_id = intent.getIntExtra("complex_id", 0)
            complex_name = intent.getStringExtra("complex_name")
            complex_line = intent.getStringExtra("complex_line")
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.title = "${apart_name}아파트 ${complex_name}동 ${complex_line}라인"
        d("complex_id", complex_id.toString())
        itemList = db.readHoData(complex_id)

        val adapter = ListAdapter(this, itemList)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        adapter.setRadioChangedListener( object : ListAdapter.RadioBtnChangedListener{
            override fun onClickedRadioButton(view: View, position: Int, idRes: Int) {
                var selectedPosition = 0
                when(idRes){
                    //0: 미완료, 1:처리완료, 2:불량, 3:기타
                    R.id.rbDone -> {
                        selectedPosition = WORK_STATUS_DONE
                    }
                    R.id.rbInComplete -> {
                        selectedPosition = WORK_STATUS_INCOPLETE
                    }
                    R.id.rbBad -> {
                        selectedPosition = WORK_STATUS_BAD
                    }
                    R.id.rbEtc -> {
                        selectedPosition = WORK_STATUS_ETC
                    }
                }
                update(complex_id, itemList[position].ho, selectedPosition)
                itemList[position].isCompletion = selectedPosition

            }
        })
    }
    fun update(complex_id: Int, ho: String, isCompletion: Int){
        d("UPDATE", "$complex_id $ho $isCompletion")
        db.updateHo(complex_id, ho, isCompletion)
    }
}
