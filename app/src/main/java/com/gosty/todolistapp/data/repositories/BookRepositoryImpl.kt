package com.gosty.todolistapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gosty.todolistapp.BuildConfig
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.utils.Result
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val db: FirebaseDatabase,
    private val crashlytics: FirebaseCrashlytics
) : BookRepository {
    override fun getAllBooks(): LiveData<Result<List<Book>>> = liveData {
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
                crashlytics.log(error.message)
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

    override fun getBookById(id: String): LiveData<Result<Book>> = liveData {
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        val book = MutableLiveData<Book>()
        var errorMessage = ""
        bookRef.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                book.value = snapshot.getValue(Book::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage = error.message
                crashlytics.log(error.message)
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

    override fun postBook(book: Book): LiveData<Result<String>> {
        val result = MediatorLiveData<Result<String>>()
        result.value = Result.Loading
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        bookRef.child(book.id!!).setValue(book)
            .addOnSuccessListener {
                result.value = Result.Success("Berhasil")
            }
            .addOnFailureListener {
                result.value = Result.Error(it.message.toString())
                crashlytics.log(it.message.toString())
            }
        return result
    }

    override fun updateBook(book: Book): LiveData<Result<String>> {
        val result = MediatorLiveData<Result<String>>()
        result.value = Result.Loading
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        bookRef.child(book.id!!).updateChildren(book.toMap())
            .addOnSuccessListener {
                result.value =  Result.Success("Berhasil")
            }
            .addOnFailureListener {
                result.value = Result.Error(it.message.toString())
                crashlytics.log(it.message.toString())
            }
        return result
    }

    override fun deleteBook(id: String): LiveData<Result<String>> {
        val result = MediatorLiveData<Result<String>>()
        result.value = Result.Loading
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        bookRef.child(id).removeValue()
            .addOnSuccessListener {
                result.value = Result.Success("Berhasil")
            }
            .addOnFailureListener {
                result.value = Result.Error(it.message.toString())
                crashlytics.log(it.message.toString())
            }
        return result
    }
}