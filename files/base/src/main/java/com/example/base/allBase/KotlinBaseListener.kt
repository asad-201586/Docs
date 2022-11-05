package com.example.base.allBase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.reflect.KClass

/**
 * * Created by Gurtek Singh on 1/31/2018.
 * Sachtech Solution
 * gurtek@protonmail.ch
 */
interface KotlinBaseListener {
    fun openAForResult(kClass: KClass<out AppCompatActivity>, bundle: Bundle, code: Int){}
    fun showProgress(){}
    fun hideProgress(){}
}