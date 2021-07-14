package com.theapache64.stackzy.ui.feature.liblist

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.common.*
import com.theapache64.stackzy.util.R


private const val GRID_SIZE = 4

@Composable
fun LibraryListScreen(
    viewModel: LibraryListViewModel,
    onBackClicked: () -> Unit,
    onLibraryClicked: (LibraryWrapper) -> Unit,
) {
    val searchKeyword by viewModel.searchKeyword.collectAsState()
    val librariesResp by viewModel.libsResp.collectAsState()
    val subTitle by viewModel.subTitle.collectAsState()

    // Calculating item width based on screen width
    val appItemWidth = (LocalAppWindow.current.width - (CONTENT_PADDING_HORIZONTAL * 2)) / GRID_SIZE

    CustomScaffold(
        title = R.string.libraries_list_title,
        subTitle = subTitle,
        onBackClicked = onBackClicked,
        topRightSlot = {

            // SearchBox
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = ""
                    )
                },
                singleLine = true,
                value = searchKeyword,
                label = {
                    Text(
                        text = R.string.select_app_label_search,
                    )
                },
                onValueChange = {
                    viewModel.onSearchKeywordChanged(it)
                },
                modifier = Modifier
                    .width(300.dp)
            )
        }
    ) {

        when (librariesResp) {
            is Resource.Loading -> {
                val message = (librariesResp as Resource.Loading<List<LibraryWrapper>>).message ?: ""
                LoadingAnimation(message)
            }
            is Resource.Error -> {
                Box {
                    ErrorSnackBar(
                        (librariesResp as Resource.Error<List<LibraryWrapper>>).errorData
                    )
                }
            }
            is Resource.Success -> {
                val libraries = (librariesResp as Resource.Success<List<LibraryWrapper>>).data


                Column {

                    if (libraries.isNotEmpty()) {
                        // Grid
                        LazyColumn {
                            items(
                                items = if (libraries.size > GRID_SIZE) {
                                    libraries.chunked(GRID_SIZE)
                                } else {
                                    listOf(libraries)
                                }
                            ) { librarySet ->
                                Row {
                                    librarySet.map { library ->

                                        // GridItem
                                        Selectable(
                                            modifier = Modifier.width(appItemWidth.dp),
                                            data = library,
                                            onSelected = onLibraryClicked
                                        )
                                    }
                                }

                                Spacer(
                                    modifier = Modifier.height(10.dp)
                                )
                            }
                        }

                    } else {
                        // No app found
                        FullScreenError(
                            title = "Library not found",
                            message = "Couldn't find any library with $searchKeyword",
                            image = imageResource("drawables/woman_desk.png"),
                        )
                    }


                }

            }
            null -> {
                LoadingAnimation("Preparing apps...")
            }
        }

    }
}