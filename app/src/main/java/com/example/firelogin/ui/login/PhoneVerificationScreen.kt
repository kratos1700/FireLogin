package com.example.firelogin.ui.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firelogin.components.PinView


@Composable
fun PhoneVerificationScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateToDetail: () -> Unit,
) {
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    val codeSent by loginViewModel.codeSent.collectAsState()
    val loading by loginViewModel.loading.collectAsState()
    val verificationCode by loginViewModel.verificationCode.collectAsState()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            enabled = !codeSent && !loading,
            value = phoneNumber.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { if (it.length <= 12) phoneNumber.value = it },
            placeholder = { Text(text = "Enter your phone number") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            supportingText = {
                Text(
                    text = "${phoneNumber.value.length} / 12",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = !codeSent,
            exit = scaleOut(
                targetScale = 0.5f,
                animationSpec = tween(durationMillis = 500, delayMillis = 100)
            ),
            enter = scaleIn(
                initialScale = 0.5f,
                animationSpec = tween(durationMillis = 500, delayMillis = 100)
            )
        ) {
            Button(
                enabled = !loading && !codeSent,
                onClick = {
                    if (phoneNumber.value.isEmpty() || phoneNumber.value.length < 12) {
                        Toast.makeText(context, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
                    } else {
                        loginViewModel.loginWithPhone("${phoneNumber.value}", context as Activity)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Registrar", modifier = Modifier.padding(8.dp))
            }
        }

        if (loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        AnimatedVisibility(
            visible = codeSent,
            exit = scaleOut(
                targetScale = 0.5f,
                animationSpec = tween(durationMillis = 500, delayMillis = 100)
            ),
            enter = scaleIn(
                initialScale = 0.5f,
                animationSpec = tween(durationMillis = 500, delayMillis = 100)
            )
        ) {
            Column {
                Text(text = "Enter the verification code sent to your phone", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))

                PinView(
                    modifier = Modifier.padding(16.dp),
                    pinLength = 6,
                    onPinEntered = { pin ->
                        if (pin.length == 6) {
                            loginViewModel.verifyCode(pin)
                        } else {
                            Toast.makeText(context, "Please enter a valid Code", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navigateToDetail()
        }
    }
}

