package com.gosty.todolistapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gosty.todolistapp.BuildConfig
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.utils.Result

class BookRepository private constructor(
    private val db: FirebaseDatabase
) {
    private val _resultPostBook = MutableLiveData<Result<String>>()
    val resultPostBook: LiveData<Result<String>> get() = _resultPostBook

    private val _resultUpdateBook = MutableLiveData<Result<String>>()
    val resultUpdateBook: LiveData<Result<String>> get() = _resultUpdateBook

    private val _resultDeleteBook = MutableLiveData<Result<String>>()
    val resultDeleteBook: LiveData<Result<String>> get() = _resultDeleteBook

    fun getAllBooks(): LiveData<Result<List<Book>>> = liveData {
        emit(Result.Loading)
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        val books = MutableLiveData<List<Book>>()
        var errorMessage = ""
        bookRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                books.value = snapshot.children.map {
                    it.getValue(Book::class.java)!!
                }
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage = error.message
            }
        })

        if (errorMessage == "") {
            val data: LiveData<Result<List<Book>>> = books.map {
                Result.Success(it)
            }
            emitSource(data)
        } else {
            emit(Result.Error(errorMessage))
        }
    }

    fun getBookById(id: String): LiveData<Result<Book>> = liveData {
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        val book = MutableLiveData<Book>()
        var errorMessage = ""
        bookRef.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                book.value = snapshot.getValue(Book::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage = error.message
            }
        })

        if (errorMessage == "") {
            val data: LiveData<Result<Book>> = book.map {
                Result.Success(it)
            }
            emitSource(data)
        } else {
            emit(Result.Error(errorMessage))
        }
    }

    fun postBook(book: Book) {
        _resultPostBook.postValue(Result.Loading)
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        bookRef.child(book.id!!).setValue(book)
            .addOnSuccessListener {
                _resultPostBook.postValue(Result.Success("Berhasil"))
            }
            .addOnFailureListener {
                _resultPostBook.postValue(Result.Error(it.message.toString()))
            }
    }

    fun updateBook(book: Book) {
        _resultUpdateBook.postValue(Result.Loading)
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        bookRef.child(book.id!!).updateChildren(book.toMap())
            .addOnSuccessListener {
                _resultUpdateBook.postValue(Result.Success("Berhasil"))
            }
            .addOnFailureListener {
                _resultUpdateBook.postValue(Result.Error(it.message.toString()))
            }
    }

    fun deleteBook(id: String) {
        _resultDeleteBook.postValue(Result.Loading)
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        bookRef.child(id).removeValue()
            .addOnSuccessListener {
                _resultDeleteBook.postValue(Result.Success("Berhasil"))
            }
            .addOnFailureListener {
                _resultDeleteBook.postValue(Result.Error(it.message.toString()))
            }
    }

    companion object {
        @Volatile
        private var instance: BookRepository? = null
        fun getInstance(db: FirebaseDatabase): BookRepository =
            instance ?: synchronized(this) {
                instance ?: BookRepository(db)
            }.also {
                instance = it
            }
    }
}