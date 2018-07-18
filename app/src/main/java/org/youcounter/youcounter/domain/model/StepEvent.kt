package org.youcounter.youcounter.domain.model

import android.hardware.SensorEvent

data class StepEvent(
        val event: SensorEvent,
        val newSecond: Boolean
)