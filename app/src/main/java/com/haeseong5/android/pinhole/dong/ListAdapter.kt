package com.haeseong5.android.pinhole.dong

import android.content.Context
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.haeseong5.android.pinhole.R
import com.haeseong5.android.pinhole.complex.ComplexAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_dong.view.*

class ListAdapter(private val context: Context, private val items: ArrayList<Dong>) : // items는 프로퍼티. 자바의 멤버변수
        RecyclerView.Adapter<ListAdapter.ViewHolder>(){ //Adapter클래스 상속

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
        val inflatedView  = LayoutInflater.from(parent.context).inflate(R.layout.item_dong, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = items.size

    //생성된 View 에 보여줄 데이터를 설정(set)함.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context)
        holder.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            radioBtnChangedListener.onClickedRadioButton(holder.radioGroup, position, i)
        }
    }

    // ViewHolder 단위 객체로 View의 데이터를 설정합니다
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        lateinit var radioGroup: RadioGroup
        fun bind(item: Dong, context: Context){
            view.item_dong_tv_ho.text = "${item.ho} 호"
            radioGroup = view.findViewById(R.id.item_dong_radio_group)
            setRadioButton(view ,item.isCompletion)
            d("처리여부", item.isCompletion.toString())
        }
        private fun setRadioButton(v: View, isCompletion: Int?){
            when (isCompletion){
                //0: 미완료, 1:처리완료, 2:불량, 3:기타
                0 -> v.item_dong_radio_group.check(R.id.rbInComplete)//처리완료
                1 -> v.item_dong_radio_group.check(R.id.rbDone)//미완료
                2 -> v.item_dong_radio_group.check(R.id.rbBad)//불량
                3 -> v.item_dong_radio_group.check(R.id.rbEtc)//기타
//                else -> v.item_dong_radio_group.check(R.id.rbEtc)
            }
        }
    }

}
