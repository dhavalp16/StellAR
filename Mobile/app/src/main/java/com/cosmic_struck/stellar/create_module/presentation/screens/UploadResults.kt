package com.cosmic_struck.stellar.create_module.presentation.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.create_module.presentation.UploadStatus
import com.cosmic_struck.stellar.create_module.presentation.viewmodel.CreateModuleViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator

@Composable
fun UploadStatusTracker(
    viewModel: CreateModuleViewModel
) {
    val state = viewModel.state.collectAsState().value
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Crossfade(
                targetState = state.uploadSuccess,
                animationSpec = tween(500),
                label = "UploadStatusTransition"
            ) { status ->
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (status) {
                        UploadStatus.IDLE -> {}
                        UploadStatus.LOADING -> LoadingView()
                        UploadStatus.SUCCESS -> SuccessView()
                        UploadStatus.ERROR -> ErrorView(state.error)
                    }
                }
            }
        }
    }

}


@Composable
private fun LoadingView() {
    CircularProgressIndicator(modifier = Modifier.size(48.dp))
    Spacer(modifier = Modifier.height(16.dp))
    Text("Uploading model...", style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun SuccessView() {
    Icon(
        painter = painterResource(R.drawable.checkmark),
        contentDescription = null,
        tint = Color(0xFF4CAF50),
        modifier = Modifier.size(48.dp)
    )
    Text("Upload Complete!", style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun ErrorView(message: String?) {
    Icon(
        painter = painterResource(R.drawable.cross),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error,
        modifier = Modifier.size(48.dp)
    )
    Text("Upload Failed", style = MaterialTheme.typography.titleMedium)
    Text(
        text = message ?: "Unknown error occurred",
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center
    )

}