package com.example.mysportsapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT,"
                + COLUMN_CALORIES + " INTEGER," + COLUMN_DATE + " TEXT" + ")")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addTraining(training: Training) {

        val values = ContentValues()

        values.put(COLUMN_NAME, training.getName())
        values.put(COLUMN_CALORIES, training.getCalories())
        values.put(COLUMN_DATE, training.getDate())

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    fun getTrainingsByDate(currentDate: String): List<Training> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = ?", arrayOf(currentDate))
        val trainings = mutableListOf<Training>()
        while (cursor.moveToNext()) {
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
            val caloriesIndex = cursor.getColumnIndex(COLUMN_CALORIES)
            val dateIndex = cursor.getColumnIndex(COLUMN_DATE)
            if (nameIndex == -1 || caloriesIndex == -1 || dateIndex == -1) {
                continue
            }
            val name = cursor.getString(nameIndex)
            val calories = cursor.getInt(caloriesIndex)
            val date = cursor.getString(dateIndex)
            trainings.add(Training(name, calories, date))
        }
        cursor.close()
        return trainings
    }

    fun deleteCourse(courseName: String) {
        val db = this.writableDatabase

        db.delete(TABLE_NAME, "name=?", arrayOf(courseName))
        db.close()
    }

    fun getAllTrainings(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }


    companion object {
        const val DATABASE_NAME = "training.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "training"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CALORIES = "calories"
        const val COLUMN_DATE = "date"
    }
}