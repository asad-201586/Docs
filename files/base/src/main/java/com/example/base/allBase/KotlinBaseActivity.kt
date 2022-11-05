package com.example.base.allBase

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.base.R
import java.util.*


/**
 * * Created by Gurtek Singh on 1/1/2018.
 * Sachtech Solution
 * gurtek@protonmail.ch
 */
@Keep
abstract class KotlinBaseActivity: AppCompatActivity(), KotlinBaseListener {

    private var isDialogShow: Boolean? = false

    override fun showProgress() {
        try {
            progress?.hide()
            isDialogShow = true
            progress?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun hideProgress() {
        try {
            if (this.isDialogShow == true) {
                progress?.hide()
                isDialogShow = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var progress: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDialog()
        //initBackStackListener()
    }

    fun getDialog() = progress

    override fun onPause() {
        super.onPause()
        progress?.dismiss()
    }
    private fun initDialog() {
        progress = Dialog(this)
        progress?.setContentView(R.layout.progress_layout)
        progress?.setCancelable(false)
        progress?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun setBottomUpAnimation(@TransitionRes enterAnim: Int, @TransitionRes exitAnim: Int) {
        (window.decorView as ViewGroup).isTransitionGroup = false
        val enter = TransitionInflater.from(this).inflateTransition(enterAnim)
        val exit = TransitionInflater.from(this).inflateTransition(exitAnim)
        exit.excludeTarget(android.R.id.statusBarBackground, true)
        exit.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition = enter
        window.exitTransition = exit
        window.returnTransition = exit
    }

    private val manager: FragmentManager by lazy {
        supportFragmentManager
    }


    fun forceHideKeyboard() {
        val view: View? = findViewById(android.R.id.content)
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    // night
    fun isNight(): Boolean {
        var isNightModeEnable = false
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)
        if (timeOfDay in 6..18) {
            isNightModeEnable = false
        } else if (timeOfDay in 0..5 || timeOfDay in 19..23) {
            isNightModeEnable = true
        }
        return isNightModeEnable
    }




}
