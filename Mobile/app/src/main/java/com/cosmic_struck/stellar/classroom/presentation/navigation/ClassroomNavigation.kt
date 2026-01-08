package com.cosmic_struck.stellar.classroom.presentation.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.cosmic_struck.stellar.classroom.presentation.screens.ClassroomHomeScreen
import com.cosmic_struck.stellar.classroom.presentation.viewmodel.ClassroomViewModel
import com.cosmic_struck.stellar.common.navigation.Screens

fun NavGraphBuilder.classroomGraph(navHostController: NavHostController){
    navigation(
        route = "classroom_graph/{classroom_id}",
        arguments = listOf(
            navArgument(name = "classroom_id"){
                type = NavType.StringType
            }
        ),
        startDestination = ClassroomScreens.ClassroomHomeScreen.route
    ){
        composable(
            route = ClassroomScreens.ClassroomHomeScreen.route
        ){
            val entry = remember(it){
                navHostController.getBackStackEntry("classroom_graph/{classroom_id}")
            }
            val viewmodel : ClassroomViewModel = hiltViewModel<ClassroomViewModel>(entry)
            ClassroomHomeScreen(
                navigateToModelScreen = {},
                viewmodel = viewmodel,
                navigateToCreateModuleScreen = {
                    navHostController.navigate("create_module_graph/$it")
                }
            )
        }

        composable(
            route = ClassroomScreens.ClassroomModelScreen.route
        ){
            val entry = remember(it) {
                navHostController.getBackStackEntry("classroom_graph/{classroom_id}")
            }
        }
    }
}

private sealed class ClassroomScreens(val route: String){
    object ClassroomHomeScreen : ClassroomScreens("classroom_home_screen")
    object ClassroomModelScreen : ClassroomScreens("classroom_model_screen")
}
