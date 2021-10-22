package com.theapache64.stackzy.ui.common.loading.funfact

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import com.theapache64.stackzy.data.remote.FunFact
import com.theapache64.stackzy.ui.common.CenterBox
import com.theapache64.stackzy.util.PureRandom


@Composable
fun FunFact(funFacts: Set<FunFact>) {
    val pureRandom = remember { PureRandom(funFacts) }
    var currentFunFact by remember { mutableStateOf(pureRandom.get()) }

    Text(
        text = currentFunFact.funFact,
        modifier = Modifier.clickable {
            currentFunFact = pureRandom.get()
        }
    )
}


fun main(args: Array<String>) = singleWindowApplication {
    CenterBox {
        FunFact(
            setOf(
                FunFact(id = 1, "A"),
                FunFact(id = 2, "B"),
                FunFact(id = 3, "C"),
                FunFact(id = 4, "D"),
                FunFact(id = 5, "E"),
            )
        )
    }
}