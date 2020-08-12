package com.haeseong5.android.pinhole.complex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.haeseong5.android.pinhole.R


class ComplexAdapter (internal val context: Context,
                      internal val complexList: List<Complex>): BaseAdapter() {


    //킅릭 인터페이스 정의
    interface ListBtnClickListener{
//        fun onUpdateButtonClick(view: View, position: Int)
        fun onDeleteButtonClick(view: View, position: Int)
    }
    //클릭 리스너 선언
    private lateinit var listBtnClickListener: ListBtnClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(listBtnClickListener: ListBtnClickListener) {
        this.listBtnClickListener = listBtnClickListener
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        lateinit var viewHolder: ViewHolder
        val view: View

        /* convertView 가 null 일때, 즉, 최초로 화면을 실행할 때
        ViewHolder에 각각의 뷰를 findViewByID로 설정
        마지막 태그를 holder로 설정
        * */
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_complex, null)
            viewHolder = ViewHolder()
            viewHolder.tvDong = view.findViewById(R.id.item_complex_dong)
            viewHolder.tvLine = view.findViewById(R.id.item_complex_line)
            viewHolder.btDelete = view.findViewById(R.id.item_complex_bt_delete)
            view.tag = viewHolder
        }else{
            //이미 만들어진 view가 있으므로 , tag를 통해 불러와서 대체한다.
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }
        viewHolder.tvDong?.text = "${complexList[position].dong}동"
        viewHolder.tvLine?.text = "${complexList[position].line}라인"

        viewHolder.btDelete?.setOnClickListener{
            listBtnClickListener.onDeleteButtonClick(it, position)
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return complexList[position]
    }

    override fun getItemId(position: Int): Long {
        return complexList[position].id.toLong()
    }

    override fun getCount(): Int {
        return complexList.size
    }

    //리스트뷰의 성능을 높히기 위해 사용
    inner class ViewHolder{
        var tvDong : TextView? = null
        var tvLine : TextView? = null
        var btDelete : Button? = null
    }

}