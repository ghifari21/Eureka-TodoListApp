package com.gosty.todolistapp.di

import com.google.firebase.database.FirebaseDatabase
import com.gosty.todolistapp.data.repositories.BookRepository
import com.gosty.todolistapp.data.repositories.BookRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideBookRepository(db: FirebaseDatabase): BookRepository = BookRepositoryImpl(db)
}