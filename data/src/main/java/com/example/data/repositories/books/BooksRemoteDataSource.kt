package com.example.data.repositories.books

import com.example.domain.entities.Volume

interface BooksRemoteDataSource {
    suspend fun getBooks(author: String): Result<List<Volume>>
}