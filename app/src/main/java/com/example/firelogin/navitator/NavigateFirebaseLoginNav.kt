package com.example.firelogin.navitator

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firelogin.ui.detail.DetailScreen
import com.example.firelogin.ui.login.LoginScreen
import com.example.firelogin.ui.singup.SingUpScreen


// sealed class para definir las rutas de la app
sealed class Routes (val route: String){
    object LoginScreen : Routes("login")
    object SingUpScreen : Routes("signup")
    object DetailScreen : Routes("detail")
}




@Composable
fun NavigateFirebaseLoginNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.LoginScreen.route){
        // Aquí se definen las rutas de la app
        composable(route = Routes.SingUpScreen.route){ SingUpScreen() }
        composable(route = Routes.LoginScreen.route){ LoginScreen(
            navigateToDetail = { navController.navigate(Routes.DetailScreen.route) }
        ) }
        composable(route = Routes.DetailScreen.route){ DetailScreen() }
    }

}