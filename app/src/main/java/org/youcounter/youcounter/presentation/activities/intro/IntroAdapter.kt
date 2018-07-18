package org.youcounter.youcounter.presentation.activities.intro

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.youcounter.youcounter.presentation.fragments.InitializationFragment1
import org.youcounter.youcounter.presentation.fragments.InitializationFragment2
import org.youcounter.youcounter.presentation.fragments.InitializationFragment3

class IntroAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> InitializationFragment1()
            1 -> InitializationFragment2()
            else -> InitializationFragment3()
        }
    }

    override fun getCount(): Int = 3
}