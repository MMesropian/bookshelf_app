@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.bookshelfapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelfapp.R
import com.example.bookshelfapp.ui.screens.BookShelfViewModel
import com.example.bookshelfapp.ui.screens.HomeScreen

@Composable
fun BookShelfApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val bookViewMode: BookShelfViewModel = viewModel(factory = BookShelfViewModel.Factory)
    Scaffold(
        modifier = Modifier
            //.nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        topBar = {
            BookShelfTopAppBar(scrollBehavior = scrollBehavior,
                bookViewMode = bookViewMode)
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeScreen(
                bookViewMode = bookViewMode,
                retryAction = bookViewMode::getBooks,
                contentPadding = innerPadding
            )
        }
    }
}

@Composable
fun BookShelfTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    bookViewMode: BookShelfViewModel,
    modifier: Modifier = Modifier,
) {
    var isSearch by remember { mutableStateOf(false) }
    val uiState by bookViewMode.queryUiState.collectAsState()
    var searchVolume by remember { mutableStateOf("") }
   Crossfade(
        modifier = Modifier.animateContentSize(),
        targetState = isSearch,
        label = "Search"
    ) { target ->
        if (target) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                /*.layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    val height = placeable.height * (1 - scrollBehavior.state.collapsedFraction)
                    layout(placeable.width, height.roundToInt()) {
                        placeable.place(0, 0)
                    }
                }*/,
                value = uiState.query,
                placeholder = { "Text enter search volume" },
                onValueChange = {
                    bookViewMode.updateQuery(it)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        isSearch = false
                        bookViewMode.getBooks()
                    }
                ),
                leadingIcon = {
                    IconButton(onClick = {
                        isSearch = false
                        bookViewMode.getBooks()
                    }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )

                    }
                },
                trailingIcon = {
                    IconButton(onClick = { searchVolume = "" }) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = null
                        )
                    }
                }
            )

        } else {

            TopAppBar(

                scrollBehavior = scrollBehavior,
                
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = if (uiState.isShowingBooksList) {
                    { Box {} }
                } else {
                    {
                        IconButton(onClick = { bookViewMode.navigateToBooksList() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { isSearch = !isSearch }) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                },
                modifier = modifier
            )
        }
    }

}