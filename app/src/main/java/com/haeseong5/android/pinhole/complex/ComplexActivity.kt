package com.haeseong5.android.pinhole.complex

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.haeseong5.android.pinhole.BaseActivity
import com.haeseong5.android.pinhole.DBHelper
import com.haeseong5.android.pinhole.R
import com.haeseong5.android.pinhole.Ho.HoActivity
import kotlinx.android.synthetic.main.activity_complex.*
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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.title = "${apart_name}아파트" //아파트이름 intent로 받기

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
//            Toast.makeText(this, complexList[position].id.toString(), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HoActivity::class.java)
            intent.putExtra("apart_name", apart_name)
            intent.putExtra("complex_id", complexList[position].id)
            intent.putExtra("complex_name", complexList[position].dong)
            intent.putExtra("complex_line", complexList[position].line)

            startActivity(intent)
        }
        adapter.setItemClickListener( object : ComplexAdapter.ListBtnClickListener{
            override fun onDeleteButtonClick(view: View, position: Int) {
                showNameDeleteDialog(position)

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
                showProgressDialog()
                complex.id = code.toInt()
                complexList.add(complex)
                adapter.notifyDataSetChanged()
                addAllHo(complex, code.toInt())
            }else{
                printToast("DB 등록 실패")
            }
            mAlertDialog.dismiss()
        }

        mDialogView.dialog_complex_cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
    private fun showNameDeleteDialog(position: Int){
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle("Delete")
        builder.setMessage("정말 삭제하시겠습니까?")
        builder.setPositiveButton("확인") {_, _ ->
            showProgressDialog()
            deleteData(complexList[position].id, position)
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
    private fun addAllHo(complex: Complex, id: Int){
        var count = complex.floor * complex.line.length
        for (i in 1..complex.floor) {
            var ho: String
            ho = i.toString() + "0"
            for (x in complex.line) {
                ho += x
                db.addHoData(apart_id, id, ho)
                count --
                ho = ho.substring(0, ho.lastIndexOf("0") + 1)
                d("진행률: ", "$count / ${complex.floor * complex.line.length}")
            }
        }
        dismissProgressDialog()
    }

    private fun deleteData(complex_id: Int, position: Int){
        val code: Int = db.deleteComplex(complex_id)
        if(code>-1){
            db.deleteHo(complex_id)
            complexList.removeAt(position)
        }

        adapter.notifyDataSetChanged()
        dismissProgressDialog()
    }

}
