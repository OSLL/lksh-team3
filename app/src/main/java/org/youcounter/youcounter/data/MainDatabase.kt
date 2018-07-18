package org.youcounter.youcounter.data

import com.raizlabs.android.dbflow.annotation.*
import java.util.*

@Database(version = MainDatabase.VERSION)
object MainDatabase {
    const val NAME = "YCDatabase"
    const val VERSION = 1
}

@Table(database = MainDatabase::class)
class Day {
    @PrimaryKey
    var id: Int? = null

    @Column
    @Unique
    var date: Calendar? = null

    @Column
    var steps: Int = 0

    @Column
    var stepsSeconds: Int = 0

    @Column
    var kkal: Int = 0

    @Column
    var waterConsumed: Int = 0

    /**
     * This must me 1 or 0
     */
    @Column
    var bestOfKind: Int? = null
}
