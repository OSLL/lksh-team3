package org.youcounter.youcounter.presentation.activities.information

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_information.*
import org.youcounter.youcounter.R

class InformationActivity : AppCompatActivity() {
    private fun loadData() {
        val todayCount = intent.getStringExtra("stepsData").toFloat().toDouble()
        val targetCount = intent.getStringExtra("target").toInt()
        val curTitle = intent.getStringExtra("pageTitle")
        val curType = intent.getStringExtra("type")
        val prevData = intent.getFloatArrayExtra("prevData")
        val dayData = intent.getStringArrayListExtra("dayData")
        val bestResultIntent = intent.getIntExtra("best", 0)
        stepsCount.text = todayCount.toInt().toString()
        lineGraph.curPercent = (todayCount / targetCount)
        columnGraph.curData = prevData.toList()
        curTarget.text = "цель: " + targetCount.toString()
        pageTitle.text = curTitle
        type1.text = " " + curType
        type2.text = " " + curType
        bestResult.text = bestResultIntent.toString()
        val preloadedCount = arrayListOf(Count1InColumn, Count2InColumn, Count3InColumn, Count4InColumn, Count5InColumn)
        val dayCount = arrayListOf(Day1InColumn, Day2InColumn, Day3InColumn, Day4InColumn, Day5InColumn)


        for (i in 0 until prevData.size) {
            preloadedCount[i].text = prevData[i].toInt().toString()
        }

        for (i in 0 until dayCount.size) {
            dayCount[i].text = dayData[i]
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        loadData()

        settingsReturn.setOnClickListener { finish() }
    }
}
