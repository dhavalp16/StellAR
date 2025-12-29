package com.cosmic_struck.stellar.home.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.common.components.BackgroundScaffold
import com.cosmic_struck.stellar.common.components.TabSwitcher
import com.cosmic_struck.stellar.common.navigation.Screens
import com.cosmic_struck.stellar.common.util.gridList
import com.cosmic_struck.stellar.home.presentation.ClassroomJoinStatus
import com.cosmic_struck.stellar.home.presentation.viewmodel.HomeScreenViewModel
import com.cosmic_struck.stellar.home.presentation.Options
import com.cosmic_struck.stellar.home.presentation.components.ClassroomCard
import com.cosmic_struck.stellar.home.presentation.components.CustomExpandableFAB
import com.cosmic_struck.stellar.home.presentation.components.FABItem
import com.cosmic_struck.stellar.home.presentation.components.GridItem
import com.cosmic_struck.stellar.home.presentation.components.JoinClassroomBottomSheet
import com.cosmic_struck.stellar.home.presentation.components.UserTopBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    navigateToModuleScreen: (String) -> Unit,
    navigateToClassroomHomeScreen: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel<HomeScreenViewModel>(),
    modifier: Modifier = Modifier) {

    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    BackgroundScaffold(
        floatingActionButton = {
            if(state.selected == Options.CLASSROOM){
                CustomExpandableFAB(
                    items = listOf(
                        FABItem(
                            icon = painterResource(R.drawable.add),
                            text = "Add Classroom"
                        ),
                        FABItem(
                            icon = painterResource(R.drawable.handshake),
                            text = "Join Classroom"
                        ),
                    ),
                    onItemClick = {item->
                        when(item.text){
                            "Join Classroom" -> viewModel.changeModalSheetState()
                            "Add Classroom" -> {}
                        }
                    }
                )
            }
        },
        topBar = {
            UserTopBar(
                userName = state.userName,
                userLevel = state.userLevel,
            )
        },
        color = Color.White,
        bottomBar = {}
    ) {it->

        if(state.classroomJoinStatus == ClassroomJoinStatus.JOINED){
            Toast.makeText(context,"Joined Classroom Successfully",Toast.LENGTH_SHORT).show()
            viewModel.toggleJoinClassroomStatus()
        }
        else if(state.classroomJoinStatus == ClassroomJoinStatus.ERROR){
            Toast.makeText(context,"Error Joining Classroom",Toast.LENGTH_SHORT).show()
            viewModel.toggleJoinClassroomStatus()
        }

    Box(
        modifier = it
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TabSwitcher(
                onOptionSelected = {
                    viewModel.onToggle(it)
                },
                activeTextColor = Color.White,
                nonActiveTextColor = Color.Black,
                options = listOf("Modules", "Classroom"),
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 16.dp),
            )
            var refreshing by remember { mutableStateOf(false) }
            LaunchedEffect(refreshing) {
                if (refreshing) {
                    delay(3000)
                    refreshing = false
                }
            }
            SwipeRefresh(
                state = rememberSwipeRefreshState(refreshing),
                onRefresh = {
                    refreshing = true
                    viewModel.refresh()
                }
            ) {
                if (state.selected == Options.MODULES) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .padding(horizontal = 20.dp),

                        ) {
                        items(gridList) {
                            GridItem(
                                modifier = Modifier
                                    .padding(10.dp),
                                onClick = { navigateToModuleScreen(it.navigationRoute) },
                                color = it.color,
                                title = it.title,
                                icon = it.icon
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 20.dp),

                        ) {
                        item(state.joinedClassrooms) {
                            state.joinedClassrooms.forEach { it ->
                                ClassroomCard(
                                    onClick = {
                                        navigateToClassroomHomeScreen()
                                    },
                                    classroom = it,
                                    modifier = Modifier
                                        .padding(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        JoinClassroomBottomSheet(
            modalSheetState = state.modalSheetState,
            onValueChange = viewModel::setCode,
            codeText = state.codeText,
            onDismiss = viewModel::changeModalSheetState,
            onSubmit = viewModel::joinClassroom
        )
    }
  }
}




