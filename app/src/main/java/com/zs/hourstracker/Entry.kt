package com.zs.hourstracker

data class Entry(val id: String, val hours: Double, val day: Int, val month: Int, val year: Int) {

    fun getDate(): String {
        return "$day/$month/$year"
    }
}