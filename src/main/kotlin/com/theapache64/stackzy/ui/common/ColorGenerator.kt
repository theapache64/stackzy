package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.theapache64.namethatcolor.manager.ColorNameFinder
import com.github.theapache64.namethatcolor.model.HexColor
import com.theapache64.stackzy.util.ColorUtil
import java.util.*

private val addedNameColorNames = mutableSetOf<String>()

fun main() {
    var color by mutableStateOf(getRandomColor())

    val bgColor by derivedStateOf {

        val brighterColor = ColorUtil.getBrightenedColor(color.second)

        Brush.horizontalGradient(
            colors = listOf(color.second, brighterColor)
        )
    }

    Preview {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AlphabetCircle(
                character = 'A',
                color = bgColor,
                modifier = Modifier
                    .padding(10.dp)
                    .size(60.dp)
            )

            Row {
                Button(
                    onClick = {

                        val colorName = ColorNameFinder.findColor(HexColor(color.first)).second.name
                        if (!addedNameColorNames.contains(colorName)) {
                            addedNameColorNames.add(colorName)
                            println(
                                "Color(0xff${color.first.replace("#", "")}), // $colorName"
                            )
                        }

                        color = getRandomColor()
                    }
                ) {
                    Text(text = "LIKE")
                }

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Button(
                    onClick = {
                        color = getRandomColor()
                    }
                ) {
                    Text(text = "DISLIKE")
                }
            }
        }
    }
}

private val random by lazy { Random() }
fun getRandomColor(): Pair<String, Color> {
    val randNum = random.nextInt(0xffffff + 1)
    val colorHex = String.format("#%06x", randNum)
    val javaColor = java.awt.Color.decode(colorHex)
    return Pair(
        colorHex,
        Color(
            javaColor.rgb
        )
    )
}