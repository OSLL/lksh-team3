package org.youcounter.youcounter.presentation.activities.settings

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_settings.*
import org.youcounter.youcounter.R
import org.youcounter.youcounter.data.PreferencesHelper

class SettingsActivity : AppCompatActivity() {
    lateinit var types: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        types = arrayOf("Возраст", "Рост", "Длина шага", "Вес", "Размер стакана", "Цель по шагам", "Цель по воде", "Цель по калориям")

        settingsReturn.setOnClickListener { finish() }

        val settingsItems: Map<View, Int> = mapOf(
                age to InputType.TYPE_CLASS_NUMBER,
                height to InputType.TYPE_CLASS_NUMBER,
                step to InputType.TYPE_CLASS_NUMBER,
                weight to InputType.TYPE_CLASS_NUMBER,
                glass to InputType.TYPE_CLASS_NUMBER,
                stepsTarget to InputType.TYPE_CLASS_NUMBER,
                waterTarget to InputType.TYPE_CLASS_NUMBER,
                caloriesTarget to InputType.TYPE_CLASS_NUMBER)

        for ((item, inputType) in settingsItems) {
            item.setOnClickListener { askValue(item, inputType) }
        }

        updateView()
    }

    private fun updateView() {
        age.findViewById<TextView>(R.id.header).text = "Возраст"
        age.findViewById<TextView>(R.id.value).text = PreferencesHelper
                .getSharedPreferenceInt(this,
                        PreferencesHelper.KEY_AGE, 0).toString()

        height.findViewById<TextView>(R.id.header).text = "Рост (см)"
        height.findViewById<TextView>(R.id.value).text = PreferencesHelper
                .getSharedPreferenceInt(this,
                        PreferencesHelper.KEY_HEIGHT, 0).toString()

        step.findViewById<TextView>(R.id.header).text = "Длина шага (см)"
        step.findViewById<TextView>(R.id.value).text = PreferencesHelper
                .getSharedPreferenceInt(this,
                        PreferencesHelper.KEY_STEP, 0).toString()

        weight.findViewById<TextView>(R.id.header).text = "Вес"
        weight.findViewById<TextView>(R.id.value).text = PreferencesHelper
                .getSharedPreferenceInt(this,
                        PreferencesHelper.KEY_WEIGHT, 0).toString()
        glass.findViewById<TextView>(R.id.header).text = "Размер стакана (мл)"
        glass.findViewById<TextView>(R.id.value).text = PreferencesHelper
                .getSharedPreferenceInt(this,
                        PreferencesHelper.KEY_GLASS, 0).toString()

        stepsTarget.findViewById<TextView>(R.id.header).text = "Цель по шагам"
        stepsTarget.findViewById<TextView>(R.id.value).text = PreferencesHelper
                .getSharedPreferenceInt(this,
                        PreferencesHelper.KEY_STEPS_TARGET, 0).toString()

        waterTarget.findViewById<TextView>(R.id.header).text = "Цель по воде (мл)"
        waterTarget.findViewById<TextView>(R.id.value).text = PreferencesHelper
                .getSharedPreferenceInt(this,
                        PreferencesHelper.KEY_WATER_TARGET, 0).toString()

        caloriesTarget.findViewById<TextView>(R.id.header).text = "Цель по калориям"
        caloriesTarget.findViewById<TextView>(R.id.value).text = PreferencesHelper
                .getSharedPreferenceInt(this,
                        PreferencesHelper.KEY_CALORIES_TARGET, 0).toString()

    }

    private fun askValue(settingsItem: View, inputType: Int) {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        lateinit var key: String
        val title1 =
                when (settingsItem.id) {
                    R.id.age -> {
                        key = PreferencesHelper.KEY_AGE
                        "Введите свой возраст:"
                    }
                    R.id.height -> {
                        key = PreferencesHelper.KEY_HEIGHT
                        "Введите свой рост:"
                    }
                    R.id.step -> {
                        key = PreferencesHelper.KEY_STEP
                        "Введите длину шага:"
                    }
                    R.id.stepsTarget -> {
                        key = PreferencesHelper.KEY_STEPS_TARGET
                        "Введите желаемое количество шагов в день:"
                    }
                    R.id.waterTarget -> {
                        key = PreferencesHelper.KEY_WATER_TARGET
                        "Введите желаемое количество воды в день"
                    }
                    R.id.caloriesTarget -> {
                        key = PreferencesHelper.KEY_CALORIES_TARGET
                        "Введите желаемое количество калорий в день"
                    }
                    R.id.glass -> {
                        key = PreferencesHelper.KEY_GLASS
                        "Введите размер вашего стакана"
                    }
                    else -> {
                        key = PreferencesHelper.KEY_WEIGHT
                        "Введите свой вес:"
                    }
                }
        builder.setTitle(title1)
        input.inputType = inputType
        builder.setView(input)
        builder.setPositiveButton("ОК"
        ) { _, _ ->
            PreferencesHelper.setSharedPreferenceInt(applicationContext,
                    key,
                    chechForOverflow(input.text.toString().toDouble()))

            updateView()

        }
        builder.setNegativeButton("Отмена"
        ) { dialog, _ -> dialog.cancel() }


        builder.show()
    }

    fun chechForOverflow(number: Double): Int {
        if (number > Int.MAX_VALUE) return Int.MAX_VALUE
        return number.toInt()
    }
}
