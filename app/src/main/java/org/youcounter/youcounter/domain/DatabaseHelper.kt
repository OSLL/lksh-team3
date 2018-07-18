package org.youcounter.youcounter.domain

import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.SQLite
import org.youcounter.youcounter.data.Day
import org.youcounter.youcounter.data.Day_Table
import java.text.SimpleDateFormat
import java.util.*

object DatabaseHelper {
    fun getCurrentDay(): Day {
        val query = SQLite.select().from(Day::class).queryList()
        val day = Day()
        day.date = Calendar.getInstance()
        day.steps = 0
        day.stepsSeconds = 0
        day.kkal = 0
        day.bestOfKind = 0
        day.waterConsumed = 0

        if (query.size == 0) {
            return day
        }

        return query[query.size - 1]
    }

    fun addSteps(count: Int) {
        val day = getCurrentDay()
        day.steps += count
        day.save()
    }

    fun addCal(count: Int) {
        val day = getCurrentDay()
        day.kkal += count
        day.save()
    }

    fun addWater(count: Int) {
        val day = getCurrentDay()
        day.waterConsumed += count
        day.save()
    }

    fun getLastFiveDays(): ArrayList<Day> {
        val RETURN_DAYS = 5
        val query = SQLite.select().from(Day::class).queryList()
        val result = arrayListOf<Day>()
        for (i in 0 until RETURN_DAYS) {
            val day = Day()
            day.date = Calendar.getInstance()
            day.steps = 0
            day.stepsSeconds = 0
            day.kkal = 0
            day.bestOfKind = 0
            day.waterConsumed = 0
            result.add(day)
        }

        var lastElement = RETURN_DAYS - 1

        for (i in (query.size - 1) downTo 0) {
            if (i < 0) {
                break
            }
            if (lastElement < 0) {
                break
            }
            result[lastElement] = query[i]
            lastElement--
        }

        return result
    }

    fun getLastFiveDaysStrings(): ArrayList<String> {
        val sdf = SimpleDateFormat("dd.MM")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -5)

        val result = arrayListOf<String>()
        for (i in 0..4) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
            result.add(sdf.format(cal.time))
        }
        return result
    }

    fun getBestSteps(): Int {
        val query = SQLite.select().from(Day::class).orderBy(Day_Table.steps, false).queryList()
        return query[0].steps
    }

    fun getBestCal(): Int {
        val query = SQLite.select().from(Day::class).orderBy(Day_Table.kkal, false).queryList()
        return query[0].kkal
    }

    fun getBestWater(): Int {
        val query = SQLite.select().from(Day::class).orderBy(Day_Table.waterConsumed, false).queryList()
        return query[0].waterConsumed
    }
}