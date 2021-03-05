package com.theapache64.stackzy.ui.feature.splash

import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.data.repo.LibrariesRepo
import com.theapache64.stackzy.utils.calladapter.flow.Resource
import com.toxicbakery.logging.Arbor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * Splash Screen's brain
 */
class SplashViewModel @Inject constructor(
    private val librariesRepo: LibrariesRepo,
    private val adbRepo: AdbRepo
) {

    private val _isSyncFinished = MutableStateFlow(false)
    val isSyncFinished: StateFlow<Boolean> = _isSyncFinished

    private val _syncMsg = MutableStateFlow("")
    val syncMsg: StateFlow<String> = _syncMsg

    private val _syncFailedMsg = MutableStateFlow<String?>(null)
    val syncFailedMsg: StateFlow<String?> = _syncFailedMsg

    init {
        syncData()
    }

    /**
     * To sync remote data with local
     */
    private fun syncData() {
        GlobalScope.launch {
            try {

                _syncMsg.value = "Syncing library definitions..."

                librariesRepo.getRemoteLibraries().collect {
                    when (it) {
                        is Resource.Loading -> {
                            // Both request are loading
                            _isSyncFinished.value = false
                            _syncFailedMsg.value = null
                        }

                        is Resource.Success -> {

                            // Cache libraries
                            librariesRepo.cacheLibraries(it.data)
                            Arbor.d("${librariesRepo.getCachedLibraries()?.size} libraries cached")

                            checkAdb()
                        }

                        is Resource.Error -> {
                            _syncFailedMsg.value = it.errorData
                        }

                    }
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                _syncFailedMsg.value = e.message
            }
        }
    }

    private suspend fun checkAdb() {

        if (adbRepo.isAdbStarted()) {
            _isSyncFinished.value = true
        } else {
            // Download ADB
            _syncMsg.value = "Downloading adb..."

            try {
                adbRepo.downloadAdb()
                _isSyncFinished.value = true
            } catch (e: IOException) {
                e.printStackTrace()
                _syncFailedMsg.value = e.message
            }
        }
    }

    fun onRetryClicked() {
        syncData()
    }
}