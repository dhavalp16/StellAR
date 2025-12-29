package com.cosmic_struck.stellar.common.navigation

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cosmic_struck.stellar.auth.presentation.navigation.authGraph
import com.cosmic_struck.stellar.biology.home.BiologyHomeScreen
import com.cosmic_struck.stellar.chemistry.home.ChemistryHomeScreen
import com.cosmic_struck.stellar.classroom.presentation.navigation.classroomGraph
import com.cosmic_struck.stellar.history.home.HistoryHomeScreen
import com.cosmic_struck.stellar.home.presentation.screens.HomeScreen
import com.cosmic_struck.stellar.physics.home.PhysicsHomeScreen
import com.cosmic_struck.stellar.stellar.arlab.presentation.ARLabScreen
import com.cosmic_struck.stellar.stellar.home.presentation.StellarHomeScreen
import com.cosmic_struck.stellar.stellar.models.presentation.navigation.modelNavGraph
import com.cosmic_struck.stellar.stellar.scantext.presentation.navigation.scanImageGraph
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(
    supabase: SupabaseClient,
    navHostController: NavHostController,
    modifier: Modifier = Modifier) {

        val auth = supabase.auth.currentSessionOrNull()

        Log.d("MAINNAVGRAPH",auth.toString())
        NavHost(navHostController, startDestination = if(auth!= null) Screens.HomeScreen.route else "auth") {

            composable(
                route = Screens.HomeScreen.route
            ){
                    HomeScreen(
                        navigateToModuleScreen = {
                            navHostController.navigate(it)
                        },
                        navigateToClassroomHomeScreen = {
                            navHostController.navigate("classroom_graph")
                        }
                    )
            }

            composable(route = Screens.ARLabScreen.route){
                ARLabScreen()
            }

            composable(route = Screens.StellarHomeScreen.route){
                StellarHomeScreen(
                    navHostController = navHostController,
                    navigateToScanText = {
                        navHostController.navigate("scan_image")
                    },
                )
            }


            composable(route = Screens.PhysicsHomeScreen.route){
                PhysicsHomeScreen()
            }
            composable(route = Screens.BiologyHomeScreen.route){
                BiologyHomeScreen()
            }
            composable(route = Screens.ChemistryHomeScreen.route){
                ChemistryHomeScreen()
            }
            composable(route = Screens.HistoryHomeScreen.route){
                HistoryHomeScreen()
            }

            authGraph(navHostController)
            scanImageGraph(navHostController)
            modelNavGraph(navHostController)
            classroomGraph(navHostController)

        }
    }
