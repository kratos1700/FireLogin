package com.example.firelogin.ui.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firelogin.R
import com.example.firelogin.ui.theme.Degradat1
import com.example.firelogin.ui.theme.Degradat2
import com.example.firelogin.ui.theme.Degradat3
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateToDetail: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToVerificationPhone: () -> Unit
) {

    val activity = LocalContext.current as Activity
    //  val scrollState = rememberScrollState()  // per poder fer scroll a la pantalla

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // var showPhoneLogin by remember { mutableStateOf(false) }

    val loading: Boolean by loginViewModel.loading.collectAsState()

    val googleLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {  // si el resultat es correcte

                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)  // obtenim el compte de google

                try {
                    val account =
                        task.getResult(ApiException::class.java)!!  // obtenim el compte de google amb les dades de l'usuari i el parseem a ApiException
                    loginViewModel.loginWithGoogle(account.idToken!!) {
                        navigateToDetail()
                    }


                } catch (e: ApiException) {
                    showToast("Google sign in failed: ${e.message}", activity)
                }

            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background( // per fer un degradat de colors
                brush = Brush.verticalGradient(
                    //  colors = listOf(Color.Magenta, Color.Cyan,)
                    colors = listOf(Degradat1, Degradat2, Degradat3)
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                //       .verticalScroll(scrollState)   // per poder fer scroll
                .padding(16.dp)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            if (loading) {

                Progress()
            }


            // Aquí se definen los campos de texto para el email y la contraseña
            Image(
                painter = painterResource(id = R.drawable.img_principal),
                contentDescription = "imagen login", modifier = Modifier
                    .size(200.dp)
                    .alpha(0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Firebase Login",
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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.medium

                    ),
                // esta part serveix per a que el textfield no tingui borde quan esta seleccionat (focusedBorderColor) i quan no esta seleccionat (unfocusedBorderColor

                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = LocalContentColor.current,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,

                )

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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.medium
                    ),
                // esta part serveix per a que el textfield no tingui borde quan esta seleccionat (focusedBorderColor) i quan no esta seleccionat (unfocusedBorderColor
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = LocalContentColor.current,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,

                    )
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {


                // Botón para login con número de teléfono
                FloatingActionButton(
                    onClick = {

                        navigateToVerificationPhone()

                    },
                    modifier = Modifier,

                    contentColor = Color.White,
                    containerColor = Color.Black,

                    ) {

                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "phone",
                        modifier = Modifier
                    )

                    //  Text("Login with phone number")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón para login con Google
                FloatingActionButton(
                    onClick = {

                        loginViewModel.onGoogleLoginSelected {
                            // el launcher crea lo necesari i ho gestiona google.
                            googleLauncher.launch(it.signInIntent)
                        }

                    },
                    modifier = Modifier,

                    //    colors = ButtonDefaults.buttonColors(Color.Magenta)
                    contentColor = Color.White,
                    containerColor = Color.Magenta,

                    ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google icon ",
                        modifier = Modifier

                            .size(24.dp)
                    )

                    //     Text("Login with Google")
                }


                // Boton para iniciar session con github
                Spacer(modifier = Modifier.height(12.dp))

                FloatingActionButton(
                    onClick = {
                        loginViewModel.onGithubLoginSelected(activity = activity) {
                            navigateToDetail()

                        }
                    },
                    modifier = Modifier,

                    //  colors = ButtonDefaults.buttonColors(Color.Yellow),
                    contentColor = Color.White,
                    containerColor = Color.Yellow,

                    ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_github),
                        contentDescription = "Github icon ",
                        modifier = Modifier
                            //  .padding(end = 12.dp)
                            .size(24.dp), tint = Color.Black
                    )

                    //   Text("Login with Github", color = Color.Black)
                }


                // Botón para login con Twitter
                Spacer(modifier = Modifier.height(12.dp))

                FloatingActionButton(
                    onClick = {
                        loginViewModel.onTwitterLoginSelected(activity = activity) {
                            navigateToDetail()

                        }
                    },
                    modifier = Modifier,

                    //   colors = ButtonDefaults.buttonColors(Color.Green),
                    contentColor = Color.White,
                    containerColor = Color.Green,
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_twitter),
                        contentDescription = "Twitter icon ",
                        modifier = Modifier

                            .size(24.dp), tint = Color.Black
                    )

                    //   Text("Login with Twitter", color = Color.Black)
                }



                // Botón para login con TAnonimo
                Spacer(modifier = Modifier.height(12.dp))

                FloatingActionButton(
                    onClick = {
                        loginViewModel.onAnonymousLoginSelected {
                            navigateToDetail()

                        }
                    },
                    modifier = Modifier,

                    //   colors = ButtonDefaults.buttonColors(Color.Green),
                    contentColor = Color.White,
                    containerColor = Color.Black,
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_anonymously),
                        contentDescription = "Twitter icon ",
                        modifier = Modifier

                            .size(24.dp), tint = Color.White
                    )

                    //   Text("Login with Twitter", color = Color.Black)
                }


            }
        }

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