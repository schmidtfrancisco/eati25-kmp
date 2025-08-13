package edu.eati25.kmp.movies.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import edu.eati25.kmp.movies.data.MoviesRepository
import edu.eati25.kmp.movies.data.MoviesService
import edu.eati25.kmp.movies.ui.screens.detail.DetailScreen
import edu.eati25.kmp.movies.ui.screens.detail.DetailViewModel
import edu.eati25.kmp.movies.ui.screens.home.HomeScreen
import edu.eati25.kmp.movies.ui.screens.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kmpmovies.composeapp.generated.resources.Res
import kmpmovies.composeapp.generated.resources.api_key
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val moviesRepository = rememberMoviesRepository()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onMovieClick = {
                    navController.navigate("detail/${it.id}")
                },
                viewModel = viewModel { HomeViewModel(moviesRepository) }
            )
        }
        composable(
            route = "detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backstackEntry ->
            val movieId = backstackEntry.arguments?.getInt("movieId")

            movieId?.let {
                val detailViewModel = viewModel {
                    DetailViewModel(movieId, moviesRepository)
                }
                DetailScreen(detailViewModel, onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun rememberMoviesRepository(): MoviesRepository {
    val apiKey = stringResource(Res.string.api_key)
    val httpClient = remember {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org"
                    parameters.append("api_key", apiKey)
                }
            }
        }
    }

    val moviesService = MoviesService(httpClient)

    return remember { MoviesRepository(moviesService) }
}