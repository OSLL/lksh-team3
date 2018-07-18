package org.youcounter.youcounter.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_initialization_2.*
import org.youcounter.youcounter.R
import org.youcounter.youcounter.data.PreferencesHelper
import org.youcounter.youcounter.presentation.activities.intro.IntroActivity

class InitializationFragment2 : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_initialization_2, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        continueButton2.setOnClickListener {
            if (ageHandler.text.toString() != "" &&
                    heightHandler.text.toString() != "" &&
                    stepRangeHandler.text.toString() != "" &&
                    weightHandler.text.toString() != "") {
                PreferencesHelper.setSharedPreferenceInt(activity!!.applicationContext, PreferencesHelper.KEY_AGE, ageHandler.text.toString().toInt())
                PreferencesHelper.setSharedPreferenceInt(activity!!.applicationContext, PreferencesHelper.KEY_STEP, stepRangeHandler.text.toString().toInt())
                PreferencesHelper.setSharedPreferenceInt(activity!!.applicationContext, PreferencesHelper.KEY_HEIGHT, heightHandler.text.toString().toInt())
                PreferencesHelper.setSharedPreferenceInt(activity!!.applicationContext, PreferencesHelper.KEY_WEIGHT, weightHandler.text.toString().toInt())
                PreferencesHelper.setSharedPreferenceInt(activity!!.applicationContext, PreferencesHelper.KEY_WATER_TARGET, weightHandler.text.toString().toInt() * 35)
                PreferencesHelper.setSharedPreferenceInt(activity!!.applicationContext, PreferencesHelper.KEY_STEPS_TARGET, 10000)
                PreferencesHelper.setSharedPreferenceInt(activity!!.applicationContext, PreferencesHelper.KEY_CALORIES_TARGET, weightHandler.text.toString().toInt() * 20 + heightHandler.text.toString().toInt() * 5)
                PreferencesHelper.setSharedPreferenceInt(activity!!.applicationContext, PreferencesHelper.KEY_GLASS , 250)

                (activity as IntroActivity).setCurrentItem(2)
            }
        }
        ageHandler.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (ageHandler.text.toString() == "")
                    age.setTextColor(Color.parseColor("#212121"))
                else
                    age.setTextColor(Color.parseColor("#43a047"))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        heightHandler.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (heightHandler.text.toString() == "")
                    height.setTextColor(Color.parseColor("#212121"))
                else
                    height.setTextColor(Color.parseColor("#43a047"))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        stepRangeHandler.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (stepRangeHandler.text.toString() == "")
                    stepRange.setTextColor(Color.parseColor("#212121"))
                else
                    stepRange.setTextColor(Color.parseColor("#43a047"))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        weightHandler.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (weightHandler.text.toString() == "")
                    weight.setTextColor(Color.parseColor("#212121"))
                else
                    weight.setTextColor(Color.parseColor("#43a047"))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }
}