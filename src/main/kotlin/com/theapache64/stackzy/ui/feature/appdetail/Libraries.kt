package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.local.Platform
import com.theapache64.stackzy.model.AnalysisReportWrapper
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.GradientMargin
import com.theapache64.stackzy.ui.common.Selectable


@Composable
fun Libraries(
    report: AnalysisReportWrapper,
    onLibrarySelected: (LibraryWrapper) -> Unit
) {

    if (report.libraries.isEmpty()) {
        // No libraries found
        val platform = report.platform
        if (platform is Platform.NativeKotlin || platform is Platform.NativeJava) {
            // native platform with libs
            FullScreenError(
                title = "We couldn't find any libraries",
                message = "But don't worry, we're improving our dictionary strength. Please try later",
                image = painterResource("drawables/guy.png")
            )
        } else {
            // non native platform with no libs
            FullScreenError(
                title = "// TODO : ",
                message = "${report.platform.name} dependency analysis is not yet supported",
                image = painterResource("drawables/ic_error_code.png")
            )
        }
    } else {

        LazyVerticalGrid(
            columns = GridCells.Fixed(4)
        ) {
            items(
                items = report.libraryWrappers
            ) { app ->
                Column {
                    // GridItem
                    Selectable(
                        data = app,
                        onSelected = onLibrarySelected
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }
            }

            item {
                // Gradient margin
                GradientMargin()
            }
        }
    }
}