package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.theapache64.stackzy.data.local.AndroidDevice

@Composable
fun SelectAppScreen(
    androidDevice: AndroidDevice,
    selectAppViewModel: SelectAppViewModel
) {
    Text(text = "Selected device : ${androidDevice.model}")
}