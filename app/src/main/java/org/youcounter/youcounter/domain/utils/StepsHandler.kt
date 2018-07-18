package org.youcounter.youcounter.domain.utils

import java.text.DecimalFormat

object StepsHandler {
    //returns string of distance
    fun getDistance(stepRange: Double, stepCount: Double): String? {
        return DecimalFormat("#0.0").format(stepRange * stepCount / 1000)
    }

    //return string of calories
    fun getCalories(stepRange: Double, stepCount: Double, weight: Int): String {
        return (0.5 * weight * stepRange * stepCount / 1000).toInt().toString()
    }
}