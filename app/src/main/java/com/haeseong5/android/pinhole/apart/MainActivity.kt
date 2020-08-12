package com.haeseong5.android.pinhole.apart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.haeseong5.android.pinhole.BaseActivity
import com.haeseong5.android.pinhole.DBHelper
import com.haeseong5.android.pinhole.R
import com.haeseong5.android.pinhole.complex.ComplexActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_apart.view.*
import kotlinx.android.synthetic.main.toolbar.*


/**
 * 아파트 입력
 * 아파트 동, 라인 입력, 층수 호수 자동생성
 * 체크박스 저장
 */


class MainActivity : BaseActivity(){

    //프로퍼티를 null 허용으로 선언하지 않고 프로퍼티 초기화를 미루는 방법
    //프로그래머가 책임질테니 넌 null일 필요가 없다.
    internal lateinit var db: DBHelper
    private lateinit var adapter: ApartAdapter
    internal var apartList:ArrayList<Apart> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)

        //setToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        supportActionBar?.title = getString(R.string.toolbar_title_main)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        btCreateApart.setOnClickListener {
            showNameCreateDialog()
        }
        //initData
        initData()
    }

    private fun initData(){
        apartList = db.readApart
        adapter = ApartAdapter(this@MainActivity, apartList)
        apartListView.adapter = adapter

        apartListView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, position.toString(), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ComplexActivity::class.java)
            intent.putExtra("id", apartList[position].id)
            intent.putExtra("name", apartList[position].name)
            startActivity(intent)
        }
        adapter.setItemClickListener( object : ApartAdapter.ListBtnClickListener{
            override fun onUpdateButtonClick(view: View, position: Int) {
                showNameUpdateDialog(position)
            }

            override fun onDeleteButtonClick(view: View, position: Int) {
                showNameDeleteDialog(position)
            }
        })

    }

    private fun showNameCreateDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_apart, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Create")
            .setMessage("작업할 아파트 단지명을 입력하세요.")
        val mAlertDialog = mBuilder.show()

        mDialogView.dialogApartBtnCheck.setOnClickListener {
            val name = mDialogView.dialogApartEtName.text.toString()
            add(name)
            mAlertDialog.dismiss()
        }

        mDialogView.dialogApartBtnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun showNameUpdateDialog(position: Int){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_apart, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Update")
            .setMessage("수정할 아파트 단지명을 입력하세요.")
        val mAlertDialog = mBuilder.show()
        mDialogView.dialogApartEtName.setText(apartList[position].name)

        mDialogView.dialogApartBtnCheck.setOnClickListener {
            val name = mDialogView.dialogApartEtName.text.toString()
            update(name, position)
            mAlertDialog.dismiss()
        }

        mDialogView.dialogApartBtnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun showNameDeleteDialog(position: Int){
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@MainActivity, R.style.Theme_AppCompat_Light_Dialog))
        builder.setTitle("Delete")
        builder.setMessage("작업중인 단지를 삭제하시겠습니까?")
        builder.setPositiveButton("확인") {_, _ ->
            delete(position)
        }
        builder.setNegativeButton("취소") {dialog, id ->
            dialog.dismiss()
        }
        builder.show()
    }

    //db 메소드
    private fun add(name: String){
        val apart = Apart(name)
        val code: Long = db.addApart(apart)
        d("code", code.toString())
        if(code > -1){
            apartList.add(apart)
            adapter.notifyDataSetChanged()
            Toast.makeText(this,"등록 성공",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"등록 실패",Toast.LENGTH_SHORT).show()
        }
    }

    private fun update(name: String, position: Int){
        apartList[position].name = name

        val code: Int = db.updateApart(apartList[position])
        d("code", code.toString())

        if(code > -1){
            adapter.notifyDataSetChanged()
            Toast.makeText(this,"수정 성공",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"수정 실패",Toast.LENGTH_SHORT).show()
        }
        adapter.notifyDataSetChanged()    }

    private fun delete(position: Int){
        val code: Int = db.deleteApart(apartList[position])
        d("code", code.toString())

        if(code > -1){
            apartList.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(this,"삭제 성공",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"삭제 실패",Toast.LENGTH_SHORT).show()
        }
        adapter.notifyDataSetChanged()

    }

}
