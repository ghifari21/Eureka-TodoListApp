package com.gosty.todolistapp.di

import com.google.firebase.database.FirebaseDatabase
import com.gosty.todolistapp.data.repositories.BookRepository

object Injection {
    fun provideBookRepository(): BookRepository {
        val db = FirebaseDatabase.getInstance()
        return BookRepository.getInstance(db)
    }
}