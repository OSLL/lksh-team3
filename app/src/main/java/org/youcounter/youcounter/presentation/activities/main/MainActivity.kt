package org.youcounter.youcounter.presentation.activities.main

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_info_bar.*
import kotlinx.android.synthetic.main.pop_up_additem_menu.*
import kotlinx.android.synthetic.main.pop_up_menu.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.youcounter.youcounter.R
import org.youcounter.youcounter.data.Day
import org.youcounter.youcounter.data.PreferencesHelper
import org.youcounter.youcounter.domain.DatabaseHelper
import org.youcounter.youcounter.domain.utils.StepsHandler
import org.youcounter.youcounter.presentation.activities.information.InformationActivity
import org.youcounter.youcounter.presentation.activities.intro.IntroActivity
import org.youcounter.youcounter.presentation.activities.settings.SettingsActivity
import org.youcounter.youcounter.presentation.customviews.RoundGraph
import java.util.*

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var mPresenter: MainPresenter

    private var isBlockedScrollView = false

    lateinit var sensorManager: SensorManager

    private val STEPS = 0
    private val CAL = 1
    private val WATER = 2
    private val SLEEP = 3

    private var animationFirstCoordinateY = 0
    private var animationSecondCoordinateY = 0
    private val MIN_GESTURE_LENGTH = 100
    private val ANIMATION_DURATION: Long = 130
    private var addItemOptions = mutableListOf<View>()
    private var blockTitles = mutableListOf<String>()
    private var blocksId = mutableListOf<View>()
    private var blockType = mutableListOf<String>()
    private var userAdd = mutableListOf(0, 0, 0)
    private var targetCount = mutableListOf(0, 0, 0)
    private var compute = mutableListOf(0, 0, 0)
    private var currentAddItemOption = 0
    private var standartGlassSize = 0

    private var userStepRange = 0.8
    private var userWeight = 70

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = MainPresenter()

        darkenScreenView.isEnabled = false

        //made for counting steps
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        blockTitles = mutableListOf(resources.getString(R.string.steps), resources.getString(R.string.cal), resources.getString(R.string.water))
        blockType = mutableListOf("шагов", "ккал", "мл")
        blocksId = mutableListOf(stepsBlock, calBlock, waterBlock)
        addItemOptions = mutableListOf(addItemOptionSteps, addItemOptionCal, addItemOptionWater)


        if (PreferencesHelper.getSharedPreferenceBoolean(applicationContext, "isFirstOpening", true)) {
            startActivity(Intent(this, IntroActivity::class.java))
        }

        initBlocks()

        for (block in blocksId) {
            block.setOnClickListener {
                openInformationActivity(block)
            }
        }

        settingsMenuOption.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        addActivityButton.setOnClickListener {
            showFromBottom(popUpAddItem)
        }

        showPopUpMenu.setOnClickListener {
            showFromBottom(popUpMenu)
        }

        scrollView.setOnTouchListener { _, _ ->
            isBlockedScrollView
        }

        popUpMenu.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> animationFirstCoordinateY = motionEvent.y.toInt()
                MotionEvent.ACTION_UP -> {
                    animationSecondCoordinateY = motionEvent.y.toInt()
                    if (animationSecondCoordinateY - animationFirstCoordinateY > MIN_GESTURE_LENGTH) {
                        hideFromBottom(view)
                    }
                }
            }
            true
        }

        popUpAddItem.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> animationFirstCoordinateY = motionEvent.y.toInt()
                MotionEvent.ACTION_UP -> {
                    animationSecondCoordinateY = motionEvent.y.toInt()
                    if (animationSecondCoordinateY - animationFirstCoordinateY > MIN_GESTURE_LENGTH) {
                        hideFromBottom(view)
                    }
                }
            }
            true
        }

        darkenScreenView.setOnTouchListener { _, _ ->
            hideFromBottom(popUpMenu)
            hideFromBottom(popUpAddItem)
            true
        }


        for (v in addItemOptions) {
            v.setOnClickListener {
                currentAddItemOption = addItemOptions.indexOf(v)
                setNotInputableOption(currentAddItemOption)
                paintAllAddItemOption()
                v.setBackgroundResource(R.drawable.rounded_additem_option_active)
                (v as TextView).fillTextWith(R.color.colorPrimary)
            }
        }

        addItemSendButton.setOnClickListener {
            sendInfromation(currentAddItemOption)
        }
    }

    override fun onResume() {
        super.onResume()
        reInitBlocks()
        redraw()
    }

    //For DB Work

    private fun initBlocks() {
        val currentDay = DatabaseHelper.getCurrentDay()
        compute = mutableListOf(0, 0, 0, 0)
        userAdd = mutableListOf(0, currentDay.kkal, currentDay.waterConsumed, 0)
        reInitBlocks()
    }

    private fun reInitBlocks() {
        userStepRange = (PreferencesHelper.getSharedPreferenceInt(applicationContext, PreferencesHelper.KEY_STEP, 80).toDouble() / 100)
        userWeight = PreferencesHelper.getSharedPreferenceInt(applicationContext, PreferencesHelper.KEY_WEIGHT, 70)
        standartGlassSize = PreferencesHelper.getSharedPreferenceInt(applicationContext, PreferencesHelper.KEY_GLASS, 250)

        targetCount = mutableListOf(
                PreferencesHelper.getSharedPreferenceInt(applicationContext, PreferencesHelper.KEY_STEPS_TARGET, 10000),
                PreferencesHelper.getSharedPreferenceInt(applicationContext, PreferencesHelper.KEY_CALORIES_TARGET, 2000),
                PreferencesHelper.getSharedPreferenceInt(applicationContext, PreferencesHelper.KEY_WATER_TARGET, 3000), 1000)

        for (block in blocksId) {
            val index = blocksId.indexOf(block)
            val titleBlock = block.findViewById<TextView>(R.id.blockTitle)
            titleBlock.text = blockTitles[index]
        }
    }

    fun requestUpdate(index: Int, count: Int) {
        if (index == STEPS) {
            addSteps(count)
        }
        if (index == CAL) {
            removeCal(count)
        }
        if (index == WATER) {
            addWater(count)
        }
        if (index == SLEEP) {
            sleepUpdate()
        }
    }

    fun recalcParams() {
        compute[CAL] = StepsHandler.getCalories(userStepRange, (userAdd[STEPS] + compute[STEPS]).toDouble(), userWeight).toInt()
    }

    fun redraw() {
        recalcParams()

        for (block in blocksId) {
            val index = blocksId.indexOf(block)
            val curCountOfBlock = block.findViewById<TextView>(R.id.currentCountInGraph).text.toString().toInt()
            val willCount = userAdd[index] + compute[index]
            block.findViewById<RoundGraph>(R.id.statisticGraph).curPercent = willCount.toDouble() / targetCount[index]
            block.findViewById<TextView>(R.id.currentCountInGraph).text = willCount.toString()
            val idOfSmallBlock = listOf(R.id.day1Preview, R.id.day2Preview, R.id.day3Preview, R.id.day4Preview, R.id.day5Preview)
            val prevData = getPreviusDataOfType(index)
            val prevDay = DatabaseHelper.getLastFiveDaysStrings()

            if (prevData.size == 0) {
                continue
            }

            for (item in 0 until idOfSmallBlock.size) {
                block.findViewById<LinearLayout>(idOfSmallBlock[item]).findViewById<TextView>(R.id.day).text = prevDay[item]
            }

            for (item in 0 until idOfSmallBlock.size - 1) {
                block.findViewById<LinearLayout>(idOfSmallBlock[item]).findViewById<TextView>(R.id.value).text = prevData[item].toInt().toString()
            }

            block.findViewById<LinearLayout>(idOfSmallBlock[idOfSmallBlock.size - 1]).findViewById<TextView>(R.id.value).text = willCount.toString()

        }

        val steps = blocksId[STEPS].findViewById<TextView>(R.id.currentCountInGraph).text.toString().toInt()
        infoKmData.text = StepsHandler.getDistance(userStepRange, steps.toDouble()).toString()
        infoCalData.text = (StepsHandler.getCalories(userStepRange, steps.toDouble(), userWeight).toInt() + userAdd[CAL]).toString()
        infoStepsData.text = steps.toString()
    }

    // Utils

    fun getPreviusDataOfType(needIndex: Int): ArrayList<Float> {
        var resultData = arrayListOf<Float>()
        val data = DatabaseHelper.getLastFiveDays()
        for (item in data) {
            if (needIndex == STEPS) {
                resultData.add(item.steps.toFloat())
            }
            if (needIndex == CAL) {
                resultData.add(item.kkal.toFloat())
            }
            if (needIndex == WATER) {
                resultData.add(item.waterConsumed.toFloat())
            }
        }
        return resultData
    }

    fun openInformationActivity(block: View) {
        val index = blocksId.indexOf(block)
        val titleBlock = block.findViewById<TextView>(R.id.blockTitle).text
        val curData = block.findViewById<TextView>(R.id.currentCountInGraph)
        val k = Intent(this, InformationActivity::class.java)

        val dataArray = getPreviusDataOfType(index)
        dataArray[dataArray.size - 1] = block.findViewById<LinearLayout>(R.id.day5Preview).findViewById<TextView>(R.id.value).text.toString().toInt().toFloat()

        val bestResult = when (titleBlock) {
            "Ходьба" -> Math.max(DatabaseHelper.getBestSteps(), blocksId[STEPS].findViewById<TextView>(R.id.currentCountInGraph).text.toString().toInt())
            "Калории" -> Math.max(DatabaseHelper.getBestCal(), blocksId[CAL].findViewById<TextView>(R.id.currentCountInGraph).text.toString().toInt())
            else -> Math.max(DatabaseHelper.getBestWater(), blocksId[WATER].findViewById<TextView>(R.id.currentCountInGraph).text.toString().toInt())
        }

        k.putExtra("stepsData", curData.text)
        k.putExtra("pageTitle", titleBlock)
        k.putExtra("target", targetCount[index].toString())
        k.putExtra("type", blockType[index])
        k.putExtra("prevData", dataArray.toFloatArray())
        k.putExtra("best", bestResult)
        k.putStringArrayListExtra("dayData", DatabaseHelper.getLastFiveDaysStrings())
        startActivity(k)
    }

    fun sendInfromation(activeView: Int) {
        val countStr = addItemInputField.text.toString()
        var count = 0
        if (countStr != "") {
            count = countStr.toInt()
        } else {
            try {
                count = addItemInputField.hint.toString().toInt()
            } catch (e: Exception) {
                count = 0
            }
        }
        if (activeView == CAL) {
            count = -count
        }
        userAdd[activeView] += count
        redraw()
        requestUpdate(activeView, count)
        addItemInputField.setText("")
        hideFromBottom(popUpMenu)
        hideFromBottom(popUpAddItem)
    }

    fun paintAllAddItemOption() {
        for (v in addItemOptions) {
            v.setBackgroundResource(R.drawable.rounded_additem_option)
            (v as TextView).fillTextWith(R.color.colorGray)
        }
    }

    private fun setNotInputableOption(viewId: Int) {
        if (viewId == WATER) {
            addItemInputField.hint = standartGlassSize.toString()
        } else {
            addItemInputField.setText("")
            addItemInputField.hint = resources.getString(R.string.inputFieldHint)
            addItemInputField.isEnabled = true
            addItemInputField.isActivated = true
            addItemInputField.visibility = View.VISIBLE
            addItemSendButton.text = resources.getText(R.string.add)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onSensorChanged(model: Day) {
        val steps = model.steps
        val distance = StepsHandler.getDistance(userStepRange, steps.toDouble())
        val calories = StepsHandler.getCalories(userStepRange, (steps + userAdd[CAL]).toDouble(), userWeight).toInt()
        infoMinData.text = (model.stepsSeconds / 60).toString()

        compute[STEPS] = steps
        compute[CAL] = calories

        redraw()
    }

    //Animation staff

    override fun onBackPressed() {
        when {
            popUpMenu.visibility == View.VISIBLE -> hideFromBottom(popUpMenu)
            popUpAddItem.visibility == View.VISIBLE -> hideFromBottom(popUpAddItem)
            else -> super.onBackPressed()
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun disableEnableControls(enable: Boolean, vg: ViewGroup) {
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            child.isEnabled = enable
            if (child is ViewGroup) {
                disableEnableControls(enable, child)
            }
        }
    }

    override fun showFromBottom(v: View) {
        if (v.visibility == View.VISIBLE) {
            return
        }
        darkenScreenView.isEnabled = true
        v.translationY = v.height.toFloat()
        v.visibility = View.VISIBLE
        disableEnableControls(false, scrollView)
        isBlockedScrollView = true
        val animator = v.animate()
        animator.translationY(0F).duration = ANIMATION_DURATION

        val darkBackground = darkenScreenView.animate()
        darkBackground.alpha(1F)
                .setDuration(0)
                .withEndAction {
                    if (Build.VERSION.SDK_INT >= 23) {
                        window.statusBarColor = ContextCompat.getColor(this, R.color.colorGrayTransperent)
                    }
                }

    }

    override fun hideFromBottom(v: View) {
        darkenScreenView.isEnabled = false
        disableEnableControls(true, scrollView)
        isBlockedScrollView = false
        val animator = v.animate()
        animator.translationY(v.height.toFloat())
                .setDuration(ANIMATION_DURATION)
                .withEndAction {
                    v.visibility = View.INVISIBLE
                    paintAllAddItemOption()

                    currentAddItemOption = 0
                    setNotInputableOption(currentAddItemOption)
                    addItemOptionSteps.setBackgroundResource(R.drawable.rounded_additem_option_active)
                    (addItemOptionSteps as TextView).fillTextWith(R.color.colorPrimary)
                }

        val darkBackground = darkenScreenView.animate()
        darkBackground.alpha(0F)
                .setDuration(0)
                .withEndAction {
                    if (Build.VERSION.SDK_INT >= 23) {
                        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)
                    }
                    hideKeyboard()
                }
    }


    //update db
    fun addSteps(count: Int) {
        DatabaseHelper.addSteps(count)
    }

    fun removeCal(count: Int) {
        DatabaseHelper.addCal(count)
    }

    fun addWater(count: Int) {
        DatabaseHelper.addWater(count)
    }

    fun sleepUpdate() {

    }
}

fun TextView.fillTextWith(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextColor(resources.getColor(color, resources.newTheme()))
    } else {
        this.setTextColor(resources.getColor(color))
    }
}