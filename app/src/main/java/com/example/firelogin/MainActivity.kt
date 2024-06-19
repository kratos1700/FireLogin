package com.example.firelogin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.firelogin.navitator.NavigateFirebaseLoginNav
import com.example.firelogin.navitator.Routes
import com.example.firelogin.ui.login.LoginViewModel

import com.example.firelogin.ui.theme.FireLoginTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        // SplashScreen
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FireLoginTheme {
                val loginViewModel: LoginViewModel = hiltViewModel()
                val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
                val isLoading by loginViewModel.loading.collectAsState()
                val navController = rememberNavController()

                // sistema per que la splash screen es mantingui fins que la condició isLoading sigui true
                splashScreen.setKeepOnScreenCondition {
                    isLoading
                }
                // LaunchedEffect per navegar a la pantalla de detall si l'usuari ja està loguejat
                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn) {
                        // Navigate to Detail Screen
                        navController.navigate(Routes.DetailScreen.route) {
                            popUpTo(Routes.LoginScreen.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.LoginScreen.route) {
                            popUpTo(Routes.DetailScreen.route) { inclusive = true }
                        }
                    }

                }


                Scaffold(modifier = Modifier.fillMaxSize()) {
                  NavigateFirebaseLoginNav(navController)
                }
            }
        }
    }


}









