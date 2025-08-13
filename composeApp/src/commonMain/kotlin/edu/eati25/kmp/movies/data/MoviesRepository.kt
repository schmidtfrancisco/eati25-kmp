package edu.eati25.kmp.movies.data

class MoviesRepository(
    private val moviesService: MoviesService
) {

    suspend fun getPopularMovies(): List<Movie> {
        return moviesService.getPopularMovies().results.map {
            it.toDomainMovie()
        }
    }

    suspend fun getMovieById(id: Int): Movie {
        return moviesService.getMovieDetails(id).toDomainMovie()
    }

    private fun RemoteMovie.toDomainMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview,
            releaseDate = releaseDate,
            poster = "https://image.tmdb.org/t/p/w185$posterPath",
            backdrop = backdropPath.let { "https://image.tmdb.org/t/p/w780$it" },
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity,
            voteAverage = voteAverage
        )
    }
}