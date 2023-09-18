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
    /***
     * This method to provide injection for Firebase Realtime Database instance.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    /***
     * This method to provide injection for Firebase Crashlytics instance.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    @Singleton
    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    /***
     * This method to provide injection for BookRepository instance.
     * @param db variable that contain instance of Firebase Realtime Database
     * @param crashlytics variable that contain instance of Firebase Crashlytics
     * @param context variable that contain application context
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    @Singleton
    @Provides
    fun provideBookRepository(
        db: FirebaseDatabase,
        crashlytics: FirebaseCrashlytics,
        @ApplicationContext context: Context
    ): BookRepository = BookRepositoryImpl(db, crashlytics, context)
}