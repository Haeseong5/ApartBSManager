package com.haeseong5.android.pinhole.Ho

class Ho {
    var ho: String = ""
    var isCompletion: Int? = null
    var date: String? = null
    var complex_id: Int = 0
    var apart_id: Int = 0

    constructor() {}
    companion object{
        //0: 미완료, 1:처리완료, 2:불량, 3:기타
        val WORK_STATUS_INCOPLETE: Int = 0
        val WORK_STATUS_DONE: Int = 1
        val WORK_STATUS_BAD: Int = 2
        val WORK_STATUS_ETC: Int = 3

    }
}