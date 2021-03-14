package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.util.R

/**
 * To show a basic content page
 */
const val CONTENT_PADDING_VERTICAL = 15
const val CONTENT_PADDING_HORIZONTAL = 15

@Composable
fun CustomScaffold(
    title: String,
    subTitle: String? = null,
    modifier: Modifier = Modifier,
    onBackClicked: (() -> Unit)? = null,
    topRightSlot: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = CONTENT_PADDING_HORIZONTAL.dp,
                end = CONTENT_PADDING_HORIZONTAL.dp,
                top = CONTENT_PADDING_VERTICAL.dp,
                bottom = CONTENT_PADDING_VERTICAL.dp,
            )
    ) {

        // Header
        Row(
            modifier = Modifier
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Back button
            if (onBackClicked != null) {
                IconButton(
                    onClick = onBackClicked,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronLeft,
                        contentDescription = R.string.select_app_cd_go_back
                    )
                }
            }

            // Title and Subtitle
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h5
                )

                if (subTitle != null) {
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            // Right slot (Search, Icons etc)
            if (topRightSlot != null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    topRightSlot()
                }
            }
        }

        // Content slot
        content()
    }

}