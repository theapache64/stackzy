package com.theapache64.stackzy.model

import androidx.compose.ui.graphics.Brush
import com.theapache64.stackzy.data.local.BaseAlphabetCircle
import com.theapache64.stackzy.util.ColorUtil

abstract class AlphabetCircle : BaseAlphabetCircle() {

    private val randomColor = ColorUtil.getRandomColor()
    private val brighterColor = ColorUtil.getBrightenedColor(randomColor)
    private val bgColor = Brush.horizontalGradient(
        colors = listOf(randomColor, brighterColor)
    )

    fun getGradientColor(): Brush = bgColor
}