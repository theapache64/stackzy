package com.theapache64.stackzy.ui.feature.appmasterdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.theapache64.gpa.api.Play
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.data.repo.PlayStoreRepo
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.util.ApkSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class AppMasterDetailViewModel @Inject constructor(
    private val adbRepo: AdbRepo,
    private val playStoreRepo: PlayStoreRepo
) {

}
