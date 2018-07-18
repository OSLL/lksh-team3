package org.youcounter.youcounter.presentation.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_initialization_1.*
import org.youcounter.youcounter.R
import org.youcounter.youcounter.presentation.activities.intro.IntroActivity

class InitializationFragment1 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_initialization_1, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        continueButton1.setOnClickListener {
            (activity as IntroActivity).setCurrentItem(1)
        }
    }
}
