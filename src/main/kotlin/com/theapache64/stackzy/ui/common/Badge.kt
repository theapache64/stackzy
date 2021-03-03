package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/*fun main() {
    Preview {
        Badge("1.5MB")
    }
}*/

@Composable
fun Badge(
    title: String
) {
    Text(
        text = title,
        modifier = Modifier
            .background(MaterialTheme.colors.primary, RoundedCornerShape(5.dp))
            .padding(5.dp),
        style = MaterialTheme.typography.caption,
    )
}