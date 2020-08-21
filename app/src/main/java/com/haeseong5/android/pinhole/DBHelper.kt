package com.haeseong5.android.pinhole

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log.d
import com.haeseong5.android.pinhole.apart.Apart
import com.haeseong5.android.pinhole.complex.Complex
import com.haeseong5.android.pinhole.Ho.Ho
import com.haeseong5.android.pinhole.work.Work
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DBHelper (context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object { //static
        private val DATABASE_VERSION = 5
        private val DATABASE_NAME = "PINHOLE.db"

        //Apart Table
        private val TABLE_NAME = "Apart"
        private val APART_ID = "Id"
        private val APART_NAME = "Name"

        //Complex Table
        private val TABLE_COMPLEX = "Complex"
        private val COMPLEX_ID = "Id"
        private val COMPLEX_DONG = "Dong"
        private val COMPLEX_LINE = "Line"
        private val COMPLEX_FLOOR = "Floor"
        private val COMPLEX_APART_ID = "ApartId" //외래키

        //Dong Tale
        private val TABLE_HO = "Ho"
        private val HO_NUMBER = "HoNumber"
        private val HO_IS_COMPLETION = "IsCompletion"
        private val HO_DATE = "Date"
        private val HO_COMPLEX_ID = "ComplexId" //외래키
        private val HO_APART_ID = "ApartId" //외래키

    }

    //앱에 db파일이 없을 때만 호출됨.
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY =
                "CREATE TABLE $TABLE_NAME (" +
                        "$APART_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$APART_NAME TEXT NOT NULL " +
                        ");"

        val CREATE_COMPLEX_TABLE_QUERY =
            "CREATE TABLE $TABLE_COMPLEX (" +
                    "$COMPLEX_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COMPLEX_DONG TEXT NOT NULL, " +
                    "$COMPLEX_LINE TEXT NOT NULL, " +
                    "$COMPLEX_FLOOR INTEGER NOT NULL, " +
                    "$COMPLEX_APART_ID INTEGER NOT NULL " + //외래키
                    ");"

        val CREATE_HO_TABLE_QUERY =
            "CREATE TABLE $TABLE_HO (" +
                    "$HO_NUMBER INTEGER NOT NULL, " +
                    "$HO_IS_COMPLETION INTEGER, " +
                    "$HO_DATE TEXT, " +
                    "$HO_COMPLEX_ID INTEGER NOT NULL, " +
                    "$HO_APART_ID INTEGER NOT NULL " +
                    ");"
        db!!.execSQL(CREATE_TABLE_QUERY)
        db!!.execSQL(CREATE_COMPLEX_TABLE_QUERY)
        db!!.execSQL(CREATE_HO_TABLE_QUERY)

    }

    //버전이 상향 조정됐을 때만 호출됨.
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_COMPLEX")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HO")

        onCreate(db!!)
    }

    //CRUD Method

    val readApart:ArrayList<Apart>
            get(){
                val apartList = ArrayList<Apart>()
                val selectQueryHandler = "SELECT * FROM $TABLE_NAME"
                val db = this.readableDatabase
                val cursor = db.rawQuery(selectQueryHandler, null)

                if(cursor.moveToFirst())
                {
                    do {
                        val apart = Apart()
                        apart.id = cursor.getInt(cursor.getColumnIndex(APART_ID))
                        apart.name = cursor.getString(cursor.getColumnIndex(APART_NAME))

                        apartList.add(apart)
                    }while (cursor.moveToNext())
                }
                db.close()
//                Log.d("DB 아파트리스트", apartList.size.toString())
                return apartList
            }
    fun addApart(apart: Apart): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(APART_NAME, apart.name)

        val code: Long = db.insert(TABLE_NAME, null, values)
        db.close()
        return code
    }

    fun updateApart(apart: Apart):Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(APART_ID, apart.id)
        values.put(APART_NAME, apart.name)
        //arrayOf(): 전달된 값들로 array 생성
        return db.update(TABLE_NAME, values,"$APART_ID=?", arrayOf(apart.id.toString()))
    }

    fun deleteApart(apart_id: Int): Int{
        val db = this.writableDatabase
        val code: Int
        db.delete(TABLE_HO, "$HO_APART_ID=?", arrayOf(apart_id.toString()))
        db.delete(TABLE_COMPLEX, "$COMPLEX_APART_ID=?", arrayOf(apart_id.toString()))
        code = db.delete(TABLE_NAME, "$APART_ID=?", arrayOf(apart_id.toString()))
        db.close()
        return code
    }
    //Complex DB


    //Complex db query
    fun addComplex(complex: Complex): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COMPLEX_DONG, complex.dong)
        values.put(COMPLEX_LINE, complex.line)
        values.put(COMPLEX_FLOOR, complex.floor)
        values.put(COMPLEX_APART_ID, complex.apart_id)

        val code: Long = db.insert(TABLE_COMPLEX, null, values)
        d("Code", code.toString())
        db.close()
        return code
    }

    fun readComplex(apart_id: Int):ArrayList<Complex> {
        val complexList = ArrayList<Complex>()
        val selectQueryHandler = "SELECT * FROM $TABLE_COMPLEX WHERE $COMPLEX_APART_ID= $apart_id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQueryHandler, null)

        if (cursor.moveToFirst()) {
            do {
                val complex = Complex()
                complex.id = cursor.getInt(cursor.getColumnIndex(COMPLEX_ID))
                complex.dong = cursor.getString(cursor.getColumnIndex(COMPLEX_DONG))
                complex.line = cursor.getString(cursor.getColumnIndex(COMPLEX_LINE))
                complex.floor = cursor.getInt(cursor.getColumnIndex(COMPLEX_FLOOR))
                complex.apart_id = cursor.getInt(cursor.getColumnIndex(COMPLEX_APART_ID))

                complexList.add(complex)
            } while (cursor.moveToNext())
        }
        db.close()
//        d("F", complexList.size.toString())
        return complexList
    }

    fun addHoData(apart_id: Int, complex_id: Int, ho: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(HO_NUMBER, ho)
        values.put(HO_COMPLEX_ID, complex_id)
        values.put(HO_APART_ID, apart_id)

        db.insert(TABLE_HO, null, values)
    }

    fun readHoData(COMPLEX_ID: Int): ArrayList<Ho>{
        val hoList = ArrayList<Ho>()
        val selectQueryHandler = "SELECT * FROM $TABLE_HO WHERE $HO_COMPLEX_ID = $COMPLEX_ID"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQueryHandler, null)

        if(cursor.moveToFirst())
        {
            do {
                val ho = Ho()
                ho.ho = cursor.getString(cursor.getColumnIndex(HO_NUMBER))
                ho.complex_id = cursor.getInt(cursor.getColumnIndex(HO_COMPLEX_ID))
                ho.isCompletion = cursor.getInt(cursor.getColumnIndex(HO_IS_COMPLETION))
                ho.date = cursor.getString(cursor.getColumnIndex(HO_DATE))
                ho.apart_id = cursor.getInt(cursor.getColumnIndex(HO_APART_ID))

                hoList.add(ho)
            }while (cursor.moveToNext())
        }
        db.close()
        return hoList
    }

    fun updateHo(complex_id: Int, ho: String, isCompletion: Int):Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(HO_IS_COMPLETION, isCompletion)
        values.put(HO_DATE, getDateTime())
        //arrayOf(): 전달된 값들로 array 생성
        return db.update(TABLE_HO, values,"$HO_COMPLEX_ID=? AND $HO_NUMBER=?", arrayOf(complex_id.toString(), ho))
    }
    private fun getDateTime(): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val date = Date()
        return dateFormat.format(date)
    }

    fun deleteComplex(complex_id: Int): Int{
        val db = this.writableDatabase
        val code: Int = db.delete(TABLE_COMPLEX, "$COMPLEX_ID=?", arrayOf(complex_id.toString()))
        db.close()
        return code
    }
    fun deleteHo(complex_id: Int): Int{
        val db = this.writableDatabase
        val code: Int = db.delete(TABLE_HO, "$HO_COMPLEX_ID=?", arrayOf(complex_id.toString()))
        db.close()
        return code
    }
    fun readAllHo(): ArrayList<Ho>{
        val hoList = ArrayList<Ho>()
        val selectQueryHandler = "SELECT * FROM $TABLE_HO "
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQueryHandler, null)

        if(cursor.moveToFirst())
        {
            do {
                val ho = Ho()
                ho.ho = cursor.getString(cursor.getColumnIndex(HO_NUMBER))
                ho.complex_id = cursor.getInt(cursor.getColumnIndex(HO_COMPLEX_ID))
                ho.isCompletion = cursor.getInt(cursor.getColumnIndex(HO_IS_COMPLETION))
                ho.date = cursor.getString(cursor.getColumnIndex(HO_DATE))
                ho.apart_id = cursor.getInt(cursor.getColumnIndex(HO_APART_ID))

                hoList.add(ho)
            }while (cursor.moveToNext())
        }
        db.close()
        return hoList
    }

}



