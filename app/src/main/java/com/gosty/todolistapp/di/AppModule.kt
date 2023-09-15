package com.gosty.todolistapp.di

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.gosty.todolistapp.data.repositories.BookRepository
import com.gosty.todolistapp.data.repositories.BookRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    @Singleton
    @Provides
    fun provideBookRepository(
        db: FirebaseDatabase,
        crashlytics: FirebaseCrashlytics,
        @ApplicationContext context: Context
    ): BookRepository = BookRepositoryImpl(db, crashlytics, context)
}