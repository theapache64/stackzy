package com.theapache64.stackzy.ui.feature.libdetail

import com.github.theapache64.gpa.model.Account
import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.repo.AuthRepo
import com.theapache64.stackzy.data.repo.ResultsRepo
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.util.ApkSource
import com.theapache64.stackzy.util.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LibraryDetailViewModel @Inject constructor(
    private val resultsRepo: ResultsRepo,
    private val authRepo: AuthRepo
) {
    private lateinit var libWrapper: LibraryWrapper
    private lateinit var viewModelScope: CoroutineScope

    private val _apps = MutableStateFlow<Resource<List<AndroidAppWrapper>>?>(null)
    val apps = _apps.asStateFlow()

    private val _pageTitle = MutableStateFlow<String>("")
    val pageTitle = _pageTitle.asStateFlow()

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword = _searchKeyword.asStateFlow()

    private lateinit var onAppSelected: (ApkSource<AndroidDeviceWrapper, Account>, AndroidAppWrapper) -> Unit
    private lateinit var onLogInNeeded: (shouldGoToPlayStore: Boolean) -> Unit

    fun init(
        viewModelScope: CoroutineScope,
        libWrapper: LibraryWrapper,
        onAppSelected: (ApkSource<AndroidDeviceWrapper, Account>, AndroidAppWrapper) -> Unit,
        onLogInNeeded: (shouldGoToPlayStore: Boolean) -> Unit
    ) {
        this.viewModelScope = viewModelScope
        this.libWrapper = libWrapper
        this.onAppSelected = onAppSelected
        this.onLogInNeeded = onLogInNeeded

        this._pageTitle.value = libWrapper.name
    }

    fun loadApps() {
        viewModelScope.launch {
            resultsRepo.getResults(libWrapper.packageName).collect {
                when (it) {
                    is Resource.Loading -> {
                        _apps.value = Resource.Loading(R.string.lib_detail_loading)
                    }
                    is Resource.Success -> {
                        val apps = it.data
                            .distinctBy { result -> result.packageName }
                            .map { result ->
                                AndroidAppWrapper(
                                    AndroidApp(
                                        appPackage = Package(name = result.packageName),
                                        isSystemApp = false,
                                        versionCode = result.versionCode,
                                        versionName = result.versionName,
                                        appTitle = result.appName,
                                        imageUrl = result.logoImageUrl
                                    ),
                                    shouldUseVersionNameAsSubTitle = true
                                )
                            }
                        _apps.value = Resource.Success(apps)
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    fun onAppClicked(appWrapper: AndroidAppWrapper) {
        viewModelScope.launch {
            authRepo.getAccount()?.let { account ->
                onAppSelected(ApkSource.PlayStore(account), appWrapper)
            } ?: onLogInNeeded(false)
        }
    }

}