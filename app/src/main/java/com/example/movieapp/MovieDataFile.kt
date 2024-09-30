package com.example.movieapp

data class Movie(
    val imdbID: String,
    val Title: String,
    val Year: String,
    val Type: String,
    val Poster: String
)

data class SearchResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String
)