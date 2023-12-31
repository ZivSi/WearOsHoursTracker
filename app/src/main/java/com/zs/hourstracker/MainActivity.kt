package com.zs.hourstracker

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zs.hourstracker.databinding.ActivityMainBinding

class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var entries: ArrayList<Entry>
    private lateinit var scrollView: ScrollView
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalHoursThisMonthTextView: TextView
    private lateinit var entriesThisMonthTextView: TextView
    private lateinit var salaryThisMonthTextView: TextView
    private val databaseHelper = DatabaseHelper(this)

    private var totalHours = 0.0
    private var salary = 0.0

    val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        val currentYear = Utility.getCurrentYear()
        val currentMonth = Utility.getCurrentMonth()


        entriesThisMonthTextView.text =
            getString(R.string.entries_this_month, Utility.getMonthName(currentMonth))

        entries = databaseHelper.readEntriesByMonthAndYear(currentMonth, currentYear)

        totalHours = Utility.countHours(entries)
        salary = Utility.calculateSalary(totalHours)

        totalHoursThisMonthTextView.text =
            getString(R.string.hours_this_month, totalHours.toString())
        salaryThisMonthTextView.text =
            getString(R.string.salary_this_month, salary.toString())

        initMonthPicker()
        initializeRecyclerView(entries)
    }

    private fun initViews() {
        entries = ArrayList()
        scrollView = binding.scrollView
        scrollView.requestFocus()
        recyclerView = binding.recyclerView
        totalHoursThisMonthTextView = binding.hoursThisMonthTextView
        entriesThisMonthTextView = binding.entriesThisMonthTextView
        salaryThisMonthTextView = binding.salaryThisMonthTextView
    }

    private fun initMonthPicker() {
        binding.monthPicker.minValue = 1
        binding.monthPicker.maxValue = 12
        binding.monthPicker.displayedValues = arrayOf(
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
        binding.monthPicker.value = Utility.getCurrentMonth()

        // This is very expensive so wait 1 second before loading the entries, the user may want to change the month again
        binding.monthPicker.setOnValueChangedListener { _, _, _ ->
            handler.removeCallbacksAndMessages(null)

            handler.postDelayed({
                val currentYear = Utility.getCurrentYear()
                // Current month is based on the index of the displayed values
                val currentMonth = binding.monthPicker.value

                entries = databaseHelper.readEntriesByMonthAndYear(currentMonth, currentYear)

                totalHours = Utility.countHours(entries)

                salary = Utility.calculateSalary(totalHours)

                totalHoursThisMonthTextView.text =
                    getString(R.string.hours_this_month, totalHours.toString())

                salaryThisMonthTextView.text =
                    getString(R.string.salary_this_month, salary.toString())

                entriesThisMonthTextView.text =
                    getString(R.string.entries_this_month, Utility.getMonthName(currentMonth))

                initializeRecyclerView(entries)
            }, 1000)
        }

    }

    private fun initializeRecyclerView(entries: ArrayList<Entry>) {
        val adapter = EntriesAdapter(entries)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(this,
            androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))

        setLongClickToDeleteEntry(adapter)
    }

    private fun setLongClickToDeleteEntry(adapter: EntriesAdapter) {
        adapter.setOnItemLongClickListener { position ->

            showDeleteDialog(position)
        }
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this).setTitle("Confirmation")
            .setMessage("Are you sure you want to delete this entry?")
            .setPositiveButton("Yes") { _, _ ->
                val entryDeleted = deleteEntry(position)
                updateUI(entryDeleted)
            }.setNegativeButton("No") { _, _ ->
            }.show()
    }

    private fun updateUI(entryDeleted: Entry) {
        totalHours -= entryDeleted.hours
        salary = Utility.calculateSalary(totalHours)

        totalHoursThisMonthTextView.text =
            getString(R.string.hours_this_month, totalHours.toString())
        salaryThisMonthTextView.text =
            getString(R.string.salary_this_month, salary.toString())
    }

    private fun deleteEntry(position: Int): Entry {
        val entry = entries[position]
        databaseHelper.deleteById(entry.id)
        entries.removeAt(position)
        recyclerView.adapter?.notifyItemRemoved(position)
        recyclerView.adapter?.notifyItemRangeChanged(position, entries.size)

        return entry
    }

    fun addEntry(view: View) {
        val intent = android.content.Intent(this, AddEntryActivity::class.java)
        startActivity(intent)

        finish()
    }
}