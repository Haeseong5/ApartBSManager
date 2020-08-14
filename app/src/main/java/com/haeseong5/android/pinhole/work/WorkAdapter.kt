package com.haeseong5.android.pinhole.work

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haeseong5.android.pinhole.R
import kotlinx.android.synthetic.main.item_ho.view.*
import kotlinx.android.synthetic.main.item_work.view.*
import org.w3c.dom.Text


class WorkAdapter(private val context: Context, private val items: ArrayList<Work>) : // items는 프로퍼티. 자바의 멤버변수
        RecyclerView.Adapter<WorkAdapter.ViewHolder>(){ //Adapter클래스 상속

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView  = LayoutInflater.from(parent.context).inflate(R.layout.item_work, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = items.size

    //생성된 View 에 보여줄 데이터를 설정(set)함.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // ViewHolder 단위 객체로 View의 데이터를 설정합니다
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(work: Work){
            view.item_work_tv_date.text = work.date
            view.item_work_tv_status.text = "${work.workStatus.toString()}건"
        }
    }

}
