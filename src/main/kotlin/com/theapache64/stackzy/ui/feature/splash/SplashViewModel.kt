package com.theapache64.stackzy.ui.feature.splash

import com.theapache64.stackzy.App
import com.theapache64.stackzy.data.remote.Config
import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.data.repo.ConfigRepo
import com.theapache64.stackzy.data.repo.FunFactsRepo
import com.theapache64.stackzy.data.repo.LibrariesRepo
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.toxicbakery.logging.Arbor
import kotlinx.coroutines.CoroutineScope
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
    private val adbRepo: AdbRepo,
    private val configRepo: ConfigRepo,
    private val funFactsRepo: FunFactsRepo
) {

    private lateinit var viewModelScope: CoroutineScope
    private val _isSyncFinished = MutableStateFlow(false)
    val isSyncFinished: StateFlow<Boolean> = _isSyncFinished

    private val _syncMsg = MutableStateFlow("")
    val syncMsg: StateFlow<String> = _syncMsg

    private val _syncFailedMsg = MutableStateFlow<String?>(null)
    val syncFailedMsg: StateFlow<String?> = _syncFailedMsg


    fun init(scope: CoroutineScope) {
        this.viewModelScope = scope
    }

    /**
     * To sync remote data with local
     */
    fun syncData() {
        viewModelScope.launch {
            try {
                syncAndCacheLibraries { // first cache libs
                    syncAndStoreConfig { // then sync config
                        syncAndStoreFunFacts {
                            checkAndFixAdb { // then check adb
                                checkJdk {
                                    _isSyncFinished.value = true // all done
                                }
                            }
                        }
                    }
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                _syncFailedMsg.value = e.message
            }
        }
    }

    private fun checkJdk(onExist: () -> Unit) {
        try {
            Runtime.getRuntime().exec("java -version");
            // jdk exists, pass callback
            onExist()
        } catch (e: IOException) {
            _syncFailedMsg.value = "Ohh no! It looks like you don't have JDK installed 😥"
        }
    }

    private suspend fun syncAndStoreFunFacts(
        onSynced: suspend () -> Unit
    ) {
        funFactsRepo.getRemoteFunFacts().collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _syncMsg.value = "Getting some fun facts for you..."
                }
                is Resource.Success -> {
                    val funFacts = resource.data
                    funFactsRepo.saveFunFactsToLocal(funFacts)
                    onSynced()
                }
                is Resource.Error -> {
                    _syncFailedMsg.value = resource.errorData
                }
            }
        }
    }

    private suspend fun syncAndCacheLibraries(
        onSuccess: suspend () -> Unit
    ) {
        librariesRepo.getRemoteLibraries().collect {
            when (it) {
                is Resource.Loading -> {
                    _syncMsg.value = "Syncing library definitions..."

                    // Both request are loading
                    _isSyncFinished.value = false
                    _syncFailedMsg.value = null
                }

                is Resource.Success -> {

                    // Cache libraries
                    librariesRepo.cacheLibraries(it.data)
                    Arbor.d("${librariesRepo.getCachedLibraries()?.size} libraries cached")


                    onSuccess()
                }

                is Resource.Error -> {
                    _syncFailedMsg.value = it.errorData
                }

            }
        }
    }

    private suspend fun syncAndStoreConfig(
        onSuccess: suspend () -> Unit
    ) {
        configRepo.getRemoteConfig().collect {
            when (it) {
                is Resource.Loading -> {
                    _syncMsg.value = "Syncing config..."
                }
                is Resource.Success -> {
                    val config = it.data
                    val isConfigVerified = verifyConfig(config)
                    if (isConfigVerified) {
                        configRepo.saveConfigToLocal(config)
                        onSuccess()
                    }
                }
                is Resource.Error -> {
                    _syncFailedMsg.value = it.errorData
                }
            }
        }
    }


    private val _shouldUpdate = MutableStateFlow(false)
    val shouldUpdate: StateFlow<Boolean> = _shouldUpdate

    private fun verifyConfig(config: Config): Boolean {
        val isUpdateNeeded = App.appArgs.versionCode < config.mandatoryVersionCode // currentVersion < mandatoryVersion
        return if (isUpdateNeeded) {
            _shouldUpdate.value = true
            false
        } else {
            true
        }
    }

    private suspend fun checkAndFixAdb(
        onSuccess: suspend () -> Unit
    ) {

        if (adbRepo.isAdbStarted()) {
            onSuccess()
        } else {
            try {
                adbRepo.downloadAdb().collect { downloadProgress ->

                    _syncMsg.value = "Setting up for the first time... $downloadProgress%"

                    if (downloadProgress == 100) {
                        onSuccess()
                    }
                }
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