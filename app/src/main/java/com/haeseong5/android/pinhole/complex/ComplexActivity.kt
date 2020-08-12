package com.haeseong5.android.pinhole.complex

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.haeseong5.android.pinhole.BaseActivity
import com.haeseong5.android.pinhole.DBHelper
import com.haeseong5.android.pinhole.R
import com.haeseong5.android.pinhole.apart.ApartAdapter
import com.haeseong5.android.pinhole.dong.DongActivity
import kotlinx.android.synthetic.main.activity_complex.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_complex.view.*
import kotlinx.android.synthetic.main.toolbar.*

class ComplexActivity : BaseActivity() {
    internal lateinit var db: DBHelper
    private var apart_id: Int = 0
    private var apart_name: String? = null

    private lateinit var adapter: ComplexAdapter
    internal var complexList:ArrayList<Complex> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complex)
        db = DBHelper(this@ComplexActivity)
        if (intent.hasExtra("id") && intent.hasExtra("name")) {
            apart_id = intent.getIntExtra("id", 0)
            apart_name = intent.getStringExtra("name")
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = apart_name //아파트이름 intent로 받기
        supportActionBar?.setDisplayUseLogoEnabled(true)

        complex_bt_create.setOnClickListener{
            showCreateDialog()
        }
        initData()
    }

    private fun initData(){
        complexList = db.readComplex
        adapter = ComplexAdapter(this, complexList)
        complex_listview.adapter = adapter

        complex_listview.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, complexList[position].id.toString(), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DongActivity::class.java)
            intent.putExtra("apart_name", apart_name)
            intent.putExtra("complex_id", complexList[position].id)
            intent.putExtra("complex_name", complexList[position].dong)
            intent.putExtra("complex_line", complexList[position].line)

            startActivity(intent)
        }
        adapter.setItemClickListener( object : ComplexAdapter.ListBtnClickListener{
            override fun onDeleteButtonClick(view: View, position: Int) {
//                showNameDeleteDialog(position)
            }
        })
    }

    private fun showCreateDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_complex, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("작업할 동/라인/층수 를 입력하세요.")
            .setMessage("호수가 자동생성됩니다.")
        val mAlertDialog = mBuilder.show()

        mDialogView.dialog_complex_check.setOnClickListener {
            val dong = mDialogView.dialog_complex_et_dong.text.toString()
            val line = mDialogView.dialog_complex_et_line.text.toString()
            val floor = mDialogView.dialog_complex_et_floor.text.toString().toInt()
            val complex = Complex()
            complex.dong = dong
            complex.line = line
            complex.floor = floor
            complex.apart_id = apart_id

            val code = db.addComplex(complex)
            if (code > -1){
                complexList.add(complex)
                adapter.notifyDataSetChanged()
            }else{
                printToast("DB 등록 실패")
            }

            mAlertDialog.dismiss()
        }

        mDialogView.dialog_complex_cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
}
