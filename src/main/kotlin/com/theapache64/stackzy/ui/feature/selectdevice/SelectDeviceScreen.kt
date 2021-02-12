package com.theapache64.stackzy.ui.feature.selectdevice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.ui.common.ContentScreen
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.util.R

@Composable
fun SelectDeviceScreen(
    selectDeviceViewModel: SelectDeviceViewModel,
    onDeviceSelected: (AndroidDevice) -> Unit
) {

    val devices by selectDeviceViewModel.connectedDevices.collectAsState()
    Content(
        devices = devices,
        onDeviceSelected = {
            selectDeviceViewModel.removeConnectionWatcher()
            onDeviceSelected(it)
        }
    )
}

@Composable
fun Content(
    devices: List<AndroidDevice>,
    onDeviceSelected: (AndroidDevice) -> Unit
) {
    if (devices.isEmpty()) {
        FullScreenError(
            title = R.string.device_no_device_title,
            message = R.string.device_no_device_message,
        )
    } else {
        ContentScreen(
            title = R.string.device_select_the_device
        ) {
            LazyColumn {
                items(devices) { device ->
                    DeviceItem(
                        device = device,
                        onDeviceSelected = onDeviceSelected
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }
            }
        }

    }
}


@Composable
fun DeviceItem(
    device: AndroidDevice,
    onDeviceSelected: (AndroidDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    var isHovered by remember { mutableStateOf(false) }
    val backgroundAlpha = if (isHovered) {
        0.2f
    } else {
        0f
    }

    Row(
        modifier = modifier
            .width(400.dp)
            .background(MaterialTheme.colors.secondary.copy(alpha = backgroundAlpha), RoundedCornerShape(5.dp))
            .clickable {
                onDeviceSelected(device)
            }
            .pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    false
                },
                onExit = {
                    isHovered = false
                    false
                }
            ).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            bitmap = imageResource("drawables/ic_smartphone.png"),
            modifier = Modifier.size(50.dp),
            contentDescription = R.string.device_cd_device_icon,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = device.model
        )
    }
}


