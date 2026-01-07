package com.cosmic_struck.stellar.create_module.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.create_module.presentation.components.ImagePicker
import com.cosmic_struck.stellar.create_module.presentation.viewmodel.CreateModuleViewModel

@Composable
fun CreateModuleScreen(
    viewmodel: CreateModuleViewModel = hiltViewModel<CreateModuleViewModel>(),
    modifier: Modifier = Modifier) {

    val state = viewmodel.state.value

    val pdfPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {it->
            viewmodel.uploadPdf(it)
        }
    )

    Scaffold {it->
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            ImagePicker(
                onChangeImage = viewmodel::uploadImage
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = {
                    viewmodel.changeDescription(it)
                },
                placeholder = {
                    Text(
                        text = "Add Description",
                        fontFamily = Rajdhani,
                        color = Color.Gray,
                    )
                }
            )

            Spacer(modifier = Modifier.height(80.dp))

            Button(
                onClick = {
                    pdfPicker.launch("application/pdf")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(state.pdfPath != null) Color.Green else Color.White,
                    contentColor = if(state.pdfPath != null) Color.White else Color.Black
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
//                    Icon(
//                        painter = painterResource(R.drawable.pdf),
//                        contentDescription = null,
//                        modifier = Modifier.size(40.dp)
//                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Upload PDF",
                        fontFamily = Rajdhani,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}