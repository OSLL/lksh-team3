package org.youcounter.youcounter.presentation

import android.app.Application
import android.content.Intent
import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import org.youcounter.youcounter.domain.StepsService

class App : Application() {

    fun ss() {
        try {
            startService(Intent(applicationContext, StepsService::class.java))
        } catch (e: Exception) {
            ss()
        }
    }

    override fun onCreate() {
        super.onCreate()
        initDatabase()
        ss()
    }

    private fun initDatabase() {
        FlowManager.init(FlowConfig.builder(applicationContext)
                .addDatabaseConfig(DatabaseConfig.builder(Database::class.java)
                        .build())
                .build())
    }

}