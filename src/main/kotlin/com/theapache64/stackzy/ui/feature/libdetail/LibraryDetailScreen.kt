package com.theapache64.stackzy.ui.feature.libdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.ui.common.CustomScaffold
import com.theapache64.stackzy.ui.common.ErrorSnackBar
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.Selectable
import com.theapache64.stackzy.ui.common.loading.LoadingAnimation
import com.theapache64.stackzy.util.R

@Composable
fun LibraryDetailScreen(
    viewModel: LibraryDetailViewModel,
    onBackClicked: () -> Unit,
) {
    val pageTitle by viewModel.pageTitle.collectAsState()
    val appsResp by viewModel.apps.collectAsState()
    val searchKeyword by viewModel.searchKeyword.collectAsState()


    CustomScaffold(
        title = pageTitle,
        subTitle = R.string.lib_detail_sub_title,
        onBackClicked = onBackClicked
    ) {
        when (appsResp) {
            is Resource.Loading -> {
                val message = (appsResp as Resource.Loading<List<AndroidAppWrapper>>).message ?: ""
                LoadingAnimation(message, funFacts = null)
            }
            is Resource.Error -> {
                Box {
                    ErrorSnackBar(
                        (appsResp as Resource.Error<List<AndroidAppWrapper>>).errorData
                    )
                }
            }
            is Resource.Success -> {
                val libraries = (appsResp as Resource.Success<List<AndroidAppWrapper>>).data


                Column {

                    if (libraries.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4)
                        ) {
                            items(libraries) { library ->
                                Column {
                                    // GridItem
                                    Selectable(
                                        modifier = Modifier.fillMaxWidth(),
                                        data = library,
                                        onSelected = viewModel::onAppClicked
                                    )

                                    Spacer(
                                        modifier = Modifier.height(10.dp)
                                    )
                                }
                            }
                        }

                    } else {
                        // No app found
                        FullScreenError(
                            title = "Library not found",
                            message = "Couldn't find any library with $searchKeyword",
                            image = painterResource("drawables/woman_desk.png"),
                        )
                    }
                }

            }
            null -> {
                LoadingAnimation("Preparing apps...", null)
            }
        }
    }
}