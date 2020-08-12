package com.haeseong5.android.pinhole.dong

//private val TABLE_DONG = "Dong"
//private val DONG_ID = "Id"
//private val DONG_HO = "Ho"
//private val DONG_IS_COMPLETION = "Completion"
//private val DONG_COMPLEX_ID = "ComplexId" //외래키
class Dong {
    var ho: String = ""
    var isCompletion: Int? = null
    var date: String? = null
    var complex_id: Int = 0

    constructor() {}
}