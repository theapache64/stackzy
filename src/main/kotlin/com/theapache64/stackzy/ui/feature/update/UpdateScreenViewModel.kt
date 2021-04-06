package com.theapache64.stackzy.ui.feature.update

import java.awt.Desktop
import java.net.URI
import javax.inject.Inject

class UpdateScreenViewModel @Inject constructor(

) {
    companion object {
        private const val LATEST_VERSION_URL = "https://github.com/theapache64/stackzy/releases/latest"
    }

    fun onUpdateClicked() {
        Desktop.getDesktop().browse(URI(LATEST_VERSION_URL))
    }
}