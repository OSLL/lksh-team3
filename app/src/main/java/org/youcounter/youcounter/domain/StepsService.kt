package org.youcounter.youcounter.domain

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.SQLite
import org.greenrobot.eventbus.EventBus
import org.youcounter.youcounter.data.Day
import org.youcounter.youcounter.domain.utils.DateHelper
import java.util.*

class StepsService : Service(), SensorEventListener {
    var isFirstStart = false

    private val mBinder = StepsBinder()

    private var mTimestamp = System.currentTimeMillis() / 1000L

    private lateinit var mSensorManager: SensorManager

    inner class StepsBinder : Binder() {
        internal
        val service: StepsService
            get() = this@StepsService
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (countSensor != null)
            mSensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onSensorChanged(event: SensorEvent) {

        val newTimestamp = System.currentTimeMillis() / 1000L

        val currentDate = Calendar.getInstance()

        val query = SQLite.select().from(Day::class).queryList()
        var model = Day()
        var wasHere = false
        if (!query.isEmpty()) {
            if (DateHelper.compareDates(currentDate.time, query[query.size - 1].date!!.time!!)) {
                model = query[query.size - 1]
                if (isFirstStart) {
                    model.steps++
                }
                if (newTimestamp != mTimestamp) {
                    model.stepsSeconds++
                }
                wasHere = true
            }
        }
        if (!wasHere) {
            model.date = currentDate
            model.steps = 0
            model.stepsSeconds = 0
            model.kkal = 0
            model.bestOfKind = 0
            model.waterConsumed = 0
        }
        model.save()
        EventBus.getDefault().post(model)

        isFirstStart = true

        mTimestamp = newTimestamp
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}