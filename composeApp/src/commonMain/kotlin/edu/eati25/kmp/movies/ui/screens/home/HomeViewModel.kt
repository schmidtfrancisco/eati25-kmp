package edu.eati25.kmp.movies.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.eati25.kmp.movies.data.Movie
import edu.eati25.kmp.movies.data.MoviesRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    var state by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch {
            state = UiState(isLoading = true)
            moviesRepository.movies.collect { movies ->
                if (movies.isNotEmpty()) {
                    state =
                        UiState(
                            isLoading = false,

                            movies = movies
                        )
                }
            }
        }
    }

    fun onQueryChange(query: String) {
        state = state.copy(query = query)
    }

    data class UiState(
        val isLoading: Boolean = false,
        val query: String = "",
        val movies: List<Movie> = emptyList(),
    ) {
        val filteredMovies: List<Movie>
            get() = if (query.isBlank()) {
                movies
            } else {
                movies.filter { it.title.contains(query, ignoreCase = true) }
            }
    }
}