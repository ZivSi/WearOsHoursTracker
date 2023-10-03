package com.zs.hourstracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.shawnlin.numberpicker.NumberPicker as NumberPicker
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddEntryActivity : AppCompatActivity() {
    private lateinit var scrollView: ScrollView
    private lateinit var daysNumberPicker: NumberPicker
    private lateinit var monthsNumberPicker: NumberPicker
    private lateinit var yearsNumberPicker: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        daysNumberPicker = findViewById(R.id.daysPicker)
        monthsNumberPicker = findViewById(R.id.monthPicker)
        yearsNumberPicker = findViewById(R.id.yearPicker)

        scrollView = findViewById(R.id.scrollView)
        scrollView.requestFocus()

        initializeNumberPicker()
    }

    private fun initializeNumberPicker() {
        monthsNumberPicker.minValue = 1
        monthsNumberPicker.maxValue = 12
        monthsNumberPicker.displayedValues = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
        monthsNumberPicker.value = Utility.getCurrentMonth()

        daysNumberPicker.minValue = 1
        daysNumberPicker.maxValue = Utility.getMaxDaysInMonth(monthsNumberPicker.value)
        daysNumberPicker.value = Utility.getCurrentDay()

        yearsNumberPicker.minValue = 2000
        yearsNumberPicker.maxValue = 2100
        yearsNumberPicker.value = Utility.getCurrentYear() - 2001

        monthsNumberPicker.setOnValueChangedListener { _, _, _ ->
            daysNumberPicker.maxValue = Utility.getMaxDaysInMonth(monthsNumberPicker.value)

            monthsNumberPicker.requestFocus()
        }

        daysNumberPicker.setOnValueChangedListener { _, _, _ ->
            daysNumberPicker.requestFocus()
        }

        yearsNumberPicker.setOnValueChangedListener { _, _, _ ->
            yearsNumberPicker.requestFocus()
        }
    }

    fun saveEntry(view: View) {
        val id = Utility.generateId()
        val hours = findViewById<EditText>(R.id.hoursEditText).text.toString().toDouble()
        val day = findViewById<NumberPicker>(R.id.daysPicker).value
        val month = findViewById<NumberPicker>(R.id.monthPicker).value
        val year = findViewById<NumberPicker>(R.id.yearPicker).value

        Toast.makeText(this, "Saved to $day/$month/$year", Toast.LENGTH_SHORT).show()

        val entry = Entry(id, hours, day, month, year)

        val db = DatabaseHelper(this)
        db.insertEntry(entry)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }
}