package com.theapache64.stackzy.model

import androidx.compose.ui.graphics.Brush
import com.theapache64.stackzy.data.local.BaseAlphabetCircle
import com.theapache64.stackzy.util.ColorUtil

abstract class AlphabetCircle : BaseAlphabetCircle() {

    val randomColor = ColorUtil.getRandomColor()
    val brighterColor = ColorUtil.getBrightenedColor(randomColor)
    val bgColor = Brush.horizontalGradient(
        colors = listOf(randomColor, brighterColor)
    )

    fun getGradientColor(): Brush = bgColor
}