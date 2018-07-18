package org.youcounter.youcounter.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_initialization_3.*
import org.youcounter.youcounter.R
import org.youcounter.youcounter.data.PreferencesHelper
import org.youcounter.youcounter.presentation.activities.main.MainActivity

class InitializationFragment3 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_initialization_3, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        continueButton3.setOnClickListener {
            PreferencesHelper.setSharedPreferenceBoolean(activity!!.applicationContext, "isFirstOpening", false)
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}