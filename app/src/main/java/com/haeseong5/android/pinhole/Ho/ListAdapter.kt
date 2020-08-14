package com.haeseong5.android.pinhole.Ho

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_BAD
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_DONE
import com.haeseong5.android.pinhole.Ho.Ho.Companion.WORK_STATUS_INCOPLETE
import com.haeseong5.android.pinhole.R


class ListAdapter(private val context: Context, private val items: ArrayList<Ho>) : // items는 프로퍼티. 자바의 멤버변수
        RecyclerView.Adapter<ListAdapter.ViewHolder>(){ //Adapter클래스 상속

    private var radioBtnList = arrayListOf<Int>()

    interface RadioBtnChangedListener{
        fun onClickedRadioButton(view: View, position: Int, i: Int)
    }
    private lateinit var radioBtnChangedListener: RadioBtnChangedListener

    internal fun setRadioChangedListener(radioBtnChangedListener: RadioBtnChangedListener){
        this.radioBtnChangedListener = radioBtnChangedListener
    }

    //보여줄 아이템 개수만큼 View 를 생성
    //RecyclerView 가 초기화 될 때 호출 됨.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Binding XML
        val inflatedView  = LayoutInflater.from(parent.context).inflate(R.layout.item_ho, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = items.size

    //생성된 View 에 보여줄 데이터를 설정(set)함.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.radioGroup.setOnCheckedChangeListener { _, i ->
            radioBtnChangedListener.onClickedRadioButton(holder.radioGroup, position, i)
        }
        holder.tvHo.text = "${items[position].ho}호"
        when (items[position].isCompletion){
            //0: 미완료, 1:처리완료, 2:불량, 3:기타
            WORK_STATUS_INCOPLETE -> holder.radioGroup.check(R.id.rbInComplete)//미완료
            WORK_STATUS_DONE -> holder.radioGroup.check(R.id.rbDone)//처리완료
            WORK_STATUS_BAD -> holder.radioGroup.check(R.id.rbBad)//불량
            WORK_STATUS_INCOPLETE -> holder.radioGroup.check(R.id.rbEtc)//기타
        }
    }

    // ViewHolder 단위 객체로 View의 데이터를 설정합니다
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var radioGroup: RadioGroup
        private var rbDone: RadioButton
        private var rbInComplete: RadioButton
        private var rbBad: RadioButton
        private var rbEtc: RadioButton
        var tvHo : TextView

        init {
            radioGroup = view.findViewById(R.id.item_ho_radio_group)
            rbDone = view.findViewById(R.id.rbDone)
            rbInComplete = view.findViewById(R.id.rbInComplete)
            rbBad = view.findViewById(R.id.rbBad)
            rbEtc = view.findViewById(R.id.rbEtc)
            tvHo = view.findViewById(R.id.item_ho_tv_ho)
        }
    }

}
