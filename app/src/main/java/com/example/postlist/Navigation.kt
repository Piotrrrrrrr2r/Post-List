package com.example.postlist

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.postlist.screens.main.MainScreen
import com.example.postlist.screens.postdetails.DetailsScreen
import com.example.postlist.screens.self.SelfScreen
import com.example.postlist.screens.user.UserScreen

object NavRoutes {
    const val HOME = "home"
    const val POST_DETAIL = "post/{postId}"
    const val USER_DETAIL = "user/{userId}"
    const val SELF_DETAIL = "self"
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            MainScreen(navController = navController)
        }

        composable(
            route = NavRoutes.POST_DETAIL,
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            DetailsScreen(
                postId = backStackEntry.arguments?.getInt("postId") ?: 0,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = NavRoutes.USER_DETAIL,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            UserScreen(
                userId = backStackEntry.arguments?.getInt("userId") ?: 0,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.SELF_DETAIL) {
            SelfScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}