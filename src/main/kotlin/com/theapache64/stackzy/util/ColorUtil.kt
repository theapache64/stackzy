package com.theapache64.stackzy.util

import androidx.compose.ui.graphics.Color
import kotlin.math.min

object ColorUtil {
    private const val BRIGHT_FRACTION = 0.20f

    /**
     * To get brightened version of the given color
     */
    fun getBrightenedColor(randomColor: Color): Color {
        val newRed = min(1f, (randomColor.red + (randomColor.red + 1 * BRIGHT_FRACTION)))
        val newGreen = min(1f, (randomColor.green + (randomColor.green + 1 * BRIGHT_FRACTION)))
        val newBlue = min(1f, (randomColor.blue + (randomColor.blue + 1 * BRIGHT_FRACTION)))

        /*Arbor.d("Red : ${randomColor.red} -> $newRed")
        Arbor.d("Blue : ${randomColor.blue} -> $newBlue")
        Arbor.d("Green : ${randomColor.green} -> $newGreen")
        Arbor.d("----------------------------------")*/
        return Color(
            red = newRed,
            green = newGreen,
            blue = newBlue,
        )
    }

    private val colorSet = setOf(
        Color(0xff127ad7), // Lochmara
        Color(0xff0cc845), // Malachite
        Color(0xff006d52), // Watercourse
        Color(0xff206b02), // Japanese Laurel
        Color(0xffa70371), // Flirt
        Color(0xff14805b), // Salem
        Color(0xff2443e5), // Persian Blue
        Color(0xff2f42cc), // Cerulean Blue
        Color(0xff445a92), // Chambray
        Color(0xff2c99aa), // Eastern Blue
        Color(0xffbd335b), // Night Shadz
        Color(0xff4bb516), // Christi
        Color(0xff778b01), // Olive
        Color(0xff1b48a9), // Tory Blue
        Color(0xffd5201f), // Thunderbird
        Color(0xff932615), // Tabasco
        Color(0xffee9c1b), // Carrot Orange
        Color(0xff503678), // Minsk
        Color(0xffe8961a), // Dixie
        Color(0xff204628), // Everglade
        Color(0xffe87c00), // Mango Tango
        Color(0xff009e5f), // Green Haze
        Color(0xffef4aca), // Razzle Dazzle Rose
        Color(0xff19365e), // Biscay
        Color(0xff001aea), // Blue
        Color(0xff2951df), // Royal Blue
        Color(0xff9f2b98), // Violet Eggplant
        Color(0xffe213f6), // Magenta Fuchsia
        Color(0xff1bc3a6), // Java
        Color(0xffc1033c), // Shiraz
        Color(0xff16a4d9), // Curious Blue
        Color(0xfffe389e), // Wild Strawberry
        Color(0xff340773), // Blue Diamond
        Color(0xff4408a6), // Blue Gem
        Color(0xff0b67c2), // Denim
        Color(0xff91110d), // Tamarillo
        Color(0xff481245), // Loulou
        Color(0xff1061fd), // Blue Ribbon
        Color(0xff78970b), // Limeade
        Color(0xff9810ef), // Electric Violet
        Color(0xff31a31a), // La Palma
        Color(0xff610b05), // Red Oxide
        Color(0xff5935ee), // Purple Heart
        Color(0xffb52958), // Hibiscus
        Color(0xffe60b8f), // Hollywood Cerise
        Color(0xff13012e), // Black Rock
        Color(0xff618f9d), // Gothic
        Color(0xff902745), // Camelot
        Color(0xff1a60dc), // Mariner
        Color(0xff3f42b8), // Governor Bay
        Color(0xfff80c81), // Rose
        Color(0xff2c6369), // Casal
        Color(0xff5ecc03), // Lima
        Color(0xff141e6c), // Lucky Point
        Color(0xfff32905), // Scarlet
        Color(0xff434d77), // East Bay
        Color(0xff4b1285), // Windsor
        Color(0xff3d8fc5), // Boston Blue
        Color(0xffca4afa), // Heliotrope
        Color(0xff95aa10), // Citron
        Color(0xfff51837), // Torch Red
        Color(0xff18a955), // Eucalyptus
        Color(0xffd80dc9), // Shocking Pink
        Color(0xffe77b11), // Christine
        Color(0xff55d804), // Bright Green
        Color(0xffe4531b), // Flamingo
        Color(0xffb81f40), // Maroon Flush
        Color(0xff46672e), // Chalet Green
        Color(0xffee0069), // Razzmatazz
        Color(0xff330f7b), // Persian Indigo
        Color(0xffdc0712), // Monza
        Color(0xff362288), // Meteorite
        Color(0xff299340), // Sea Green
        Color(0xff9c852c), // Luxor Gold
        Color(0xff21bbe3), // Scooter
        Color(0xffd6327d), // Cerise
        Color(0xffc729ac), // Medium Red Violet
        Color(0xff0d02d8), // Dark Blue
        Color(0xff91005f), // Fresh Eggplant
        Color(0xff60197d), // Honey Flower
        Color(0xff5e1c16), // Cherrywood
        Color(0xfff66a07), // Blaze Orange
        Color(0xff0a76a3), // Allports
        Color(0xff131954), // Bunting
    )

    private val pureRandom = PureRandom(colorSet)

    fun getRandomColor(): Color {
        return pureRandom.take()
    }

}