package org.youcounter.youcounter.domain.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    fun compareDates(date1: Date, date2: Date): Boolean {
        val sdf = SimpleDateFormat("yyyyMMdd")
        return sdf.format(date1) == sdf.format(date2)
    }
}