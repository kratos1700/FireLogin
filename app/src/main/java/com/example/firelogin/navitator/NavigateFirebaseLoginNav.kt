package com.example.firelogin.navitator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.firelogin.ui.detail.DetailScreen
import com.example.firelogin.ui.login.LoginScreen
import com.example.firelogin.ui.login.PhoneVerificationScreen
import com.example.firelogin.ui.singup.SingUpScreen


// sealed class para definir las rutas de la app
sealed class Routes(val route: String) {
    object LoginScreen : Routes("login")
    object SingUpScreen : Routes("signup")
    object DetailScreen : Routes("detail")
    object PhoneVerificationScreen : Routes("verification")
}


@Composable
fun NavigateFirebaseLoginNav(navController: NavHostController) {
    //  val navController = rememberNavController()  he canviat aquesta línia ja que li pasem per parametre per controlar el splash screen i fer les operacions oportunes
    NavHost(navController = navController, startDestination = Routes.LoginScreen.route) {

        // Aquí se definen las rutas de la app
        composable(route = Routes.SingUpScreen.route) {
            SingUpScreen(
                navigateToDetail = { navController.navigate(Routes.DetailScreen.route) }
            )
        }

        composable(route = Routes.LoginScreen.route) {
            LoginScreen(
                navigateToDetail = { navController.navigate(Routes.DetailScreen.route) },
                navigateToRegister = { navController.navigate(Routes.SingUpScreen.route) },
                navigateToVerificationPhone = { navController.navigate(Routes.PhoneVerificationScreen.route) }
            )
        }


        composable(route = Routes.DetailScreen.route) {
            DetailScreen(
                navigateToLogin = {
                    navController.navigate(Routes.LoginScreen.route) {
                        popUpTo(Routes.DetailScreen.route) { inclusive = true } // per a que no es pugui tornar enrere
                    }
                }
            )
        }

        composable(route = Routes.PhoneVerificationScreen.route) {
           PhoneVerificationScreen {pin ->

           }
        }




    }

}