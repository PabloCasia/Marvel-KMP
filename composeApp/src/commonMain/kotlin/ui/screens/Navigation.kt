package ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    data object CharacterList : Screen(route = "characterList")
    data class CharacterDetail(val id: Int) : Screen(route = "characterDetail/$id") {
        fun createRoute(): String = "characterDetail/$id"
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.CharacterList.route) {
        composable(Screen.CharacterList.route) {
            CharacterListScreen(
                onCharacterClick = { id ->
                    navController.navigate(Screen.CharacterDetail(id).createRoute())
                }
            )
        }
        composable(
            route = "characterDetail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("id")
            CharacterDetailScreen(
                characterId = characterId ?: 0,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}