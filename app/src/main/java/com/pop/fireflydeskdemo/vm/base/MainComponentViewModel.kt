package com.pop.fireflydeskdemo.vm.base

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.parcelize.Parcelize

open class MainComponentViewModel : ViewModel() {

    //控件
    open val controller: List<MainComponentController> = mutableListOf()


    open fun onControllerClick(controller: MainComponentController) {}

}

@Parcelize
data class MainComponentController(
    val desc: String,
    val iconRes: Int,
    val onClick: () -> Unit = {}
) : Parcelable