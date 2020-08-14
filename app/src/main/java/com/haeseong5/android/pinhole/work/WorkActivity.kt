package com.haeseong5.android.pinhole.work

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haeseong5.android.pinhole.DBHelper
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_BAD
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_DONE
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_INCOPLETE
import com.haeseong5.android.pinhole.Ho.ListAdapter
import com.haeseong5.android.pinhole.R
import kotlinx.android.synthetic.main.activity_ho.*
import kotlinx.android.synthetic.main.activity_work.*
import kotlinx.android.synthetic.main.toolbar.*

class WorkActivity : AppCompatActivity() {
    private var workList = arrayListOf<Work>()
    private val db: DBHelper = DBHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.title = "일별 작업현황"
        readWorkStatus()
        val adapter = WorkAdapter(this, workList)
        workRecyclerView.adapter = adapter
        workRecyclerView.setHasFixedSize(true)
        workRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        workRecyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

    }
    private fun readWorkStatus(){
        val hoList = db.readAllHo()
        d("총 생성 호 개수 ", "${hoList.size}")

        val map = mutableMapOf<String?, Int?>()
        for (ho in hoList){
            if ( ho.isCompletion == WORK_STATUS_DONE || ho.isCompletion == WORK_STATUS_BAD ){
                d("호수 ", "${ho.date}   ${ho.ho}  ${ho.isCompletion}")
                d("맵 ", "${map[ho.date]}")
                if(map[ho.date] == null){
                    map[ho.date] = 1
                }else{
                    map[ho.date] = map[ho.date]!! + 1 //!!키워드는 강제로 null 이 아님을 선언.
                }
            }
        } //end For()
        d("MAP", map["2020-08-15"].toString())
        for ((key, value) in map){
            val work = Work(key ,value)
            workList.add(work)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
