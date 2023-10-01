package com.zs.hourstracker

import java.time.LocalDate
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList

class Utility {
    companion object {
        fun getMonthName(month: Int): String {
            return when (month) {
                1 -> "January"
                2 -> "February"
                3 -> "March"
                4 -> "April"
                5 -> "May"
                6 -> "June"
                7 -> "July"
                8 -> "August"
                9 -> "September"
                10 -> "October"
                11 -> "November"
                12 -> "December"
                else -> "Invalid month"
            }
        }

        fun getMaxDaysInMonth(month: Int): Int {
            return when (month) {
                1 -> 31 // January
                2 -> 28 // February
                3 -> 31 // March
                4 -> 30 // April
                5 -> 31 // May
                6 -> 30 // June
                7 -> 31 // July
                8 -> 31 // August
                9 -> 30 // September
                10 -> 31 // October
                11 -> 30 // November
                12 -> 31 // December
                else -> -1
            }
        }


        fun countHours(entriesArray: ArrayList<Entry>): Double {
            return entriesArray.sumOf { it.hours }
        }

        fun getCurrentYear(): Int {
            return Year.now().value
        }

        fun getCurrentMonth(): Int {
            return LocalDate.now().month.value
        }

        fun calculateSalary(totalHours: Double, salaryPerHour: Int = 45): Double {
            return totalHours * salaryPerHour
        }

        fun getCurrentDay(): Int {
            return LocalDate.now().dayOfMonth
        }

        fun generateId(): String = UUID.randomUUID().toString()
    }
}