package com.theapache64.stackzy.ui.feature.devicelist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.ui.common.CustomScaffold
import com.theapache64.stackzy.ui.common.ErrorSnackBar
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.Selectable
import com.theapache64.stackzy.ui.common.loading.LoadingAnimation
import com.theapache64.stackzy.util.R

/**
 * To select a device from connected devices
 */
@Composable
fun SelectDeviceScreen(
    deviceListViewModel: DeviceListViewModel,
    onBackClicked: () -> Unit,
    onDeviceSelected: (AndroidDeviceWrapper) -> Unit
) {
    val devices by deviceListViewModel.connectedDevices.collectAsState()

    Content(
        devicesResp = devices,
        onDeviceSelected = onDeviceSelected,
        onBackClicked = onBackClicked,
        onRetry = { deviceListViewModel.watchConnectedDevices() }
    )
}

@Composable
fun Content(
    devicesResp: Resource<List<AndroidDeviceWrapper>>?,
    onDeviceSelected: (AndroidDeviceWrapper) -> Unit,
    onBackClicked: () -> Unit,
    onRetry: () -> Unit
) {
    if (devicesResp == null) {
        // Just background
        Box(
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    // Content
    CustomScaffold(
        title = R.string.device_select_the_device,
        onBackClicked = onBackClicked
    ) {

        when (devicesResp) {
            is Resource.Loading -> {
                LoadingAnimation(message = devicesResp.message ?: "", null)
            }
            is Resource.Error -> {
                ErrorSnackBar(syncFailedReason = devicesResp.errorData, onRetryClicked = onRetry)
            }
            is Resource.Success -> {
                val devices = devicesResp.data
                if (devices.isEmpty()) {
                    FullScreenError(
                        title = R.string.device_no_device_title,
                        message = R.string.device_no_device_message,
                        image = painterResource("drawables/art_click.png")
                    )
                } else {

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    LazyColumn {
                        items(devices) { device ->
                            Selectable(
                                data = device,
                                modifier = Modifier
                                    .width(400.dp),
                                onSelected = onDeviceSelected
                            )

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}





