package edu.eati25.kmp.movies.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.eati25.kmp.movies.data.Movie
import edu.eati25.kmp.movies.data.MoviesRepository
import kotlinx.coroutines.launch

class DetailViewModel(
    private val id: Int,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    var state by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch {
            state = UiState(isLoading = true)
            moviesRepository.getMovieById(id).collect { movie ->
                movie?.let {
                    state = UiState(
                        isLoading = false,
                        movie = it
                    )
                }
            }
        }
    }

    fun onFavoriteClick() {
        state.movie?.let { movie ->
            viewModelScope.launch {
                moviesRepository.toggleFavorite(movie)
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val movie: Movie? = null,
    )
}