package com.cosmic_struck.stellar.classroom.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.classroom.presentation.components.ClassroomTopAppBar
import com.cosmic_struck.stellar.classroom.presentation.components.MemberCard
import com.cosmic_struck.stellar.classroom.presentation.components.ModelCardClassroom
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.ClassroomViewModel
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.Options
import com.cosmic_struck.stellar.common.components.BackgroundScaffold
import com.cosmic_struck.stellar.common.components.TabSwitcher

@Composable
fun ClassroomHomeScreen(
    navigateToModelScreen : () -> Unit,
    viewmodel: ClassroomViewModel = hiltViewModel(),
    modifier: Modifier = Modifier) {

    val state = viewmodel.homeState.collectAsState().value

    BackgroundScaffold(
        color = Color.White,
        topBar = {
            ClassroomTopAppBar(
                classroomMembers = state.classroomMembers,
                classroomName = state.classroomName,
                classroomAuthor = state.classroomAuthor
            )
        }
    ) { it ->
        Box(
            modifier = it
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TabSwitcher(
                    modifier = Modifier
                        .height(40.dp)
                        .padding(horizontal = 16.dp),
                    options = listOf(
                        "Members",
                        "Models"
                    ),
                    onOptionSelected = {it->
                        viewmodel.onToggle(it)
                    },
                    activeTextColor = Color.White,
                    nonActiveTextColor = Color.Black
                )

                if(state.selected == Options.MEMBERS){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            state.classroomMembersList
                        ){it->
                            MemberCard(
                                name = it.user_name
                            )
                        }
                    }
                }
                else{
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                    ) {
                        items(state.classroomModelsList){it->
                            ModelCardClassroom(
                                navigateToModelScreen = { navigateToModelScreen()},
                                modelURL = it.model_url,
                                modelThumbnail = it.model_thumbnail ?: "",
                                modelName = it.model_name,
                                modelDescription = it.description ?: "No Description",
                            )
                        }
                    }
                }

            }
        }
    }
}