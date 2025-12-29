package com.cosmic_struck.stellar

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color.TRANSPARENT
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.cosmic_struck.stellar.common.di.SupabaseModule
import com.cosmic_struck.stellar.common.navigation.MainNavGraph
import com.cosmic_struck.stellar.common.work.CleanupWorker
import com.cosmic_struck.stellar.ui.theme.StellARTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var sessionDirectory: File? = null
    @Inject
    lateinit var supabaseClient: SupabaseClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(scrim = TRANSPARENT, darkScrim = TRANSPARENT)
        )

        setContent {
            StellARTheme {
                // Clean single-line call to handle all permissions
                AppPermissionsHandler()

                val navHostController = rememberNavController()
                MainNavGraph(
                    supabase = supabaseClient,
                    navHostController = navHostController
                )
            }
        }
    }


}

@Composable
fun AppPermissionsHandler() {
    val context = LocalContext.current

    // Define permissions based on Android Version
    val requiredPermissions = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            // Android 12 and below
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    // Single launcher for multiple permissions
    val multiplePermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            val permissionName = it.key
            val isGranted = it.value
            Log.d("AppPermissions", "$permissionName granted: $isGranted")

            if (!isGranted) {
                // Optionally handle denial here (e.g. show a snackbar or dialog)
                Log.w("AppPermissions", "$permissionName was denied.")
            }
        }
    }

    LaunchedEffect(Unit) {
        // Filter out permissions that are already granted
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        // If there are any missing permissions, request them
        if (permissionsToRequest.isNotEmpty()) {
            Log.d("AppPermissions", "Requesting missing permissions: $permissionsToRequest")
            multiplePermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
}