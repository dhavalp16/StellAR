package com.cosmic_struck.stellar.create_module.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.cosmic_struck.stellar.create_module.presentation.CreateModuleScreen

fun NavGraphBuilder.createModuleNavigation(navHostController: NavHostController){
    navigation(startDestination = CreateModuleScreens.CreateModuleScreen.route, route = "create_module_graph"){
        composable(route = CreateModuleScreens.CreateModuleScreen.route){
            CreateModuleScreen()
        }
    }
}

sealed class CreateModuleScreens(val route: String){
    object CreateModuleScreen : CreateModuleScreens("create_module_screen")

}