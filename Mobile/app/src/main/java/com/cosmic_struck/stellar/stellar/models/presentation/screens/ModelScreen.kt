package com.cosmic_struck.stellar.stellar.models.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.common.components.BackgroundScaffold
import com.cosmic_struck.stellar.common.components.TabSwitcher
import com.cosmic_struck.stellar.stellar.models.presentation.components.ModelTopAppBar
import com.cosmic_struck.stellar.stellar.models.presentation.components.ScoreRow
import com.cosmic_struck.stellar.stellar.models.presentation.components.PlanetCard
import com.cosmic_struck.stellar.stellar.models.presentation.viewmodel.ListType
import com.cosmic_struck.stellar.stellar.models.presentation.viewmodel.ModelScreenViewModel

@Composable
fun ModelScreen(
    navigateToModelViewer: (String,String) -> Unit,
    viewModel: ModelScreenViewModel = hiltViewModel<ModelScreenViewModel>(),
    modifier: Modifier = Modifier
) {

    BackgroundScaffold(
        topBar = {
            ModelTopAppBar()
        }
    ) {
        Column(
            modifier = it.fillMaxSize()
        ) {
            val state = viewModel.state.value
            val lazyListState = rememberLazyListState()
            val isScrollingDown = remember { mutableStateOf(false) }
            var previousScrollOffset = remember { 0 }

            // Detect scroll direction
            LaunchedEffect(lazyListState) {
                snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
                    .collect { currentOffset ->
                        isScrollingDown.value = currentOffset > previousScrollOffset
                        previousScrollOffset = currentOffset
                    }
            }

            // Animated visibility for ScoreRow
            AnimatedVisibility(
                visible =  lazyListState.firstVisibleItemScrollOffset == 0,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -it },
                )
            ) {
                ScoreRow()
            }

            Spacer(modifier = Modifier.height(10.dp))

            TabSwitcher(

                nonActiveTextColor = Color.White,
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 16.dp),
                initialIndex = state.currentIndex,
                onOptionSelected = { viewModel.onToggleList() },
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    items = if (state.currentList == ListType.MY_COLLECTION) viewModel.state.value.collectedList else viewModel.state.value.discoverList,
                ) { planet ->
                    PlanetCard(
                        locked = state.currentList != ListType.MY_COLLECTION,
                        modifier = Modifier.fillMaxWidth(),
                        navigateToModelViewer = navigateToModelViewer,
                        planet = planet,

                    )
                }
            }
        }
    }

}