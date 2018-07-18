package org.youcounter.youcounter.presentation.activities.main

import android.view.View

interface MainView {
    fun showFromBottom(v: View)
    fun hideFromBottom(v: View)
}