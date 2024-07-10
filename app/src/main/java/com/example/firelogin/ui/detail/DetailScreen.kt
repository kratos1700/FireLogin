package com.example.firelogin.ui.detail

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun DetailScreen(detailViewModel: DetailViewModel = hiltViewModel(), navigateToLogin: () -> Unit ) {
   // val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp ).padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Aquí se define el contenido de la pantalla de detalle

        Button(
            onClick = { detailViewModel.logout(){
                navigateToLogin()
               // activity.finishAffinity()    // Cerrar todas las actividades
            } },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }

}