package org.youcounter.youcounter.presentation.activities.intro

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_intro.*
import org.youcounter.youcounter.R

class IntroActivity : AppCompatActivity() {
    private lateinit var vpAdapter: IntroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        initViewPager()
    }

    fun initViewPager() {
        vpAdapter = IntroAdapter(supportFragmentManager)
        viewPagerIntro.adapter = vpAdapter
    }

    fun setCurrentItem(index: Int) {
        viewPagerIntro.currentItem = index
    }

    override fun onBackPressed() {
        return
    }
}