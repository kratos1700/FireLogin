package com.example.firelogin.ui.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firelogin.R
import com.example.firelogin.components.DialogPhone


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateToDetail: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToVerificationPhone: () -> Unit
) {

    val activity = LocalContext.current as Activity

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPhoneLogin by remember { mutableStateOf(false) }

    val loading: Boolean by loginViewModel.loading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        if (loading) {

            Progress()
        }


        // Aquí se definen los campos de texto para el email y la contraseña
        Image(
            painter = painterResource(id = R.drawable.img_principal),
            contentDescription = "imagen login", modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )



        Text(
            text = "Registrate",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.End)
                .padding(12.dp)
                .clickable {
                    navigateToRegister()
                }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botón para login con email y contraseña
        Button(
            onClick = {
                loginViewModel.login(email, password) {
                    // Navegar a la siguiente pantalla
                    navigateToDetail()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "mail",
                modifier = Modifier.padding(end = 12.dp)
            )

            Text("Login with email and password")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para login con número de teléfono
        Button(
            onClick = {
                showPhoneLogin = true

            },
            modifier = Modifier
                .fillMaxWidth(),

            colors = ButtonDefaults.buttonColors(Color.Black)

        ) {

            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "phone",
                modifier = Modifier.padding(end = 12.dp)
            )

            Text("Login with phone number")
        }


    }

    DialogPhone(
        showDialog = showPhoneLogin,
        onDismiss = { showPhoneLogin = false }) { phoneNumber ->
        loginViewModel.loginWithPhone(
            phoneNumber, activity = activity,
            onVerificationCompleted = { navigateToDetail() },
            onVerificationFailed = {
                showToast("Ha havbido un error: $it", activity)
            },
            onCodeSend = {
                         navigateToVerificationPhone()

                //
            }

            )

    }
}


@Composable
fun Progress() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color.Blue, modifier = Modifier.size(100.dp))
    }

}


private fun showToast(msg: String, activity: Activity) {
    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
}