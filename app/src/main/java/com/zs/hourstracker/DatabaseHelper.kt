package com.zs.hourstracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "HoursTracker.db"
        private const val TABLE_NAME = "entries"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // UUID will be used as primary key
        val statement = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "id TEXT PRIMARY KEY, " +
                "hours REAL, " +
                "day INTEGER, " +
                "month INTEGER, " +
                "year INTEGER);"

        db.execSQL(statement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun deleteById(id: String) {
        val db = writableDatabase

        val selection = "id = ?"
        val selectionArgs = arrayOf(id)

        db.delete(TABLE_NAME, selection, selectionArgs)

        db.close()
    }

    fun readEntriesByMonthAndYear(month: Int, year: Int): ArrayList<Entry> {
        val db = readableDatabase

        val projection = arrayOf("id", "hours", "day", "month", "year")

        val selection = "month = ? AND year = ?"
        val selectionArgs = arrayOf(month.toString(), year.toString())

        val sortOrder = "month ASC, day ASC"

        val cursor = db.query(
            TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )

        val entries = ArrayList<Entry>()

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow("id"))
                val hours = getDouble(getColumnIndexOrThrow("hours"))
                val day = getInt(getColumnIndexOrThrow("day"))
                val month = getInt(getColumnIndexOrThrow("month"))
                val year = getInt(getColumnIndexOrThrow("year"))

                entries.add(Entry(id, hours, day, month, year))
            }
        }

        db.close()

        return entries
    }


    fun insertEntry(entry: Entry) {
        val db = writableDatabase

        val values = ContentValues()

        Log.d("DatabaseHelper", "year: ${entry.year}, month: ${entry.month} day: ${entry.day} hours: ${entry.hours} id: ${entry.id}")

        values.put("id", entry.id)
        values.put("hours", entry.hours)
        values.put("day", entry.day)
        values.put("month", entry.month)
        values.put("year", entry.year)

        val newRowId = db.insertOrThrow(TABLE_NAME, null, values)

        db.close()

        Log.d("DatabaseHelper", "New row id: $newRowId")
    }
}