package com.gosty.todolistapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gosty.todolistapp.data.repositories.BookRepository
import com.gosty.todolistapp.di.Injection
import com.gosty.todolistapp.ui.add.AddViewModel
import com.gosty.todolistapp.ui.detail.DetailViewModel
import com.gosty.todolistapp.ui.edit.EditViewModel
import com.gosty.todolistapp.ui.main.MainViewModel

class ViewModelFactory private constructor(private val bookRepository: BookRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(bookRepository) as T
            modelClass.isAssignableFrom(AddViewModel::class.java) -> AddViewModel(bookRepository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(bookRepository) as T
            modelClass.isAssignableFrom(EditViewModel::class.java) -> EditViewModel(bookRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideBookRepository()
                )
            }.also {
                instance = it
            }
    }
}