package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.local.AnalysisReport
import com.theapache64.stackzy.data.local.Platform
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.ui.common.CONTENT_PADDING_HORIZONTAL
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.GradientMargin
import com.theapache64.stackzy.ui.common.Selectable

private const val GRID_SIZE = 4

@Composable
fun Libraries(
    report: AnalysisReport,
    onLibrarySelected: (Library) -> Unit
) {

    if (report.libraries.isEmpty()) {
        // No libraries found
        val platform = report.platform
        if (platform is Platform.NativeKotlin || platform is Platform.NativeJava) {
            // native platform with libs
            FullScreenError(
                title = "We couldn't find any libraries",
                message = "But don't worry, we're improving our dictionary strength. Please try later",
                image = imageResource("drawables/guy.png")
            )
        } else {
            // non native platform with no libs
            FullScreenError(
                title = "// TODO : ",
                message = "${report.platform.name} dependency analysis is yet to support",
                image = imageResource("drawables/ic_error_code.png")
            )
        }
    } else {

        // Calculating item width based on screen width
        val appItemWidth = (LocalAppWindow.current.width - (CONTENT_PADDING_HORIZONTAL * 2)) / GRID_SIZE

        LazyColumn {
            items(
                items = if (report.libraries.size > GRID_SIZE) {
                    report.libraries.chunked(GRID_SIZE)
                } else {
                    listOf(report.libraries)
                }
            ) { appSet ->
                Row {
                    appSet.map { app ->

                        // GridItem
                        Selectable(
                            modifier = Modifier.width(appItemWidth.dp),
                            data = app,
                            onSelected = onLibrarySelected
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }

            item {
                // Gradient margin
                GradientMargin()
            }
        }
    }
}