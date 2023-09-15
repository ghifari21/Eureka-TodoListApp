package com.gosty.todolistapp.data.repositories

import android.content.Context
import android.net.ConnectivityManager
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
    private val crashlytics: FirebaseCrashlytics,
    private val context: Context
) : BookRepository {
    /***
     * This method to get all the book collection from realtime database.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    override fun getAllBooks(): LiveData<Result<List<Book>>> {
        val result = MediatorLiveData<Result<List<Book>>>()
        result.value = Result.Loading
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)

        if (isInternetAvailable()) {
            bookRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                     val data = snapshot.children.map {
                         it.getValue(Book::class.java)!!
                     }
                    result.value = Result.Success(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    result.value = Result.Error(error.message)
                    crashlytics.log(error.message)
                }
            })
        } else {
            result.value = Result.Error("Connection Error, Please Check Your Connection.")
        }

        return result
    }

    /***
     * This method to get book based on the id from realtime database.
     * @param id variable that indicate book ID
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    override fun getBookById(id: String): LiveData<Result<Book>> {
        val result = MediatorLiveData<Result<Book>>()
        result.value = Result.Loading
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)

        if (isInternetAvailable()) {
            bookRef.child(id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(Book::class.java)!!
                    result.value = Result.Success(data)
                }

                override fun onCancelled(error: DatabaseError) {
                    result.value = Result.Error(error.message)
                    crashlytics.log(error.message)
                }
            })
        } else {
            result.value = Result.Error("Connection Error, Please Check Your Connection.")
        }

        return result
    }

    /***
     * This method to add the book data to realtime database.
     * @param book variable that contain book model
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     * @see Book
     */
    override fun postBook(book: Book): LiveData<Result<String>> {
        val result = MediatorLiveData<Result<String>>()
        result.value = Result.Loading
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        if (isInternetAvailable()) {
            bookRef.child(book.id!!).setValue(book)
                .addOnSuccessListener {
                    result.value = Result.Success("Berhasil")
                }
                .addOnFailureListener {
                    result.value = Result.Error(it.message.toString())
                    crashlytics.log(it.message.toString())
                }
        } else {
            result.value = Result.Error("Connection Error, Please Check Your Connection.")
        }
        return result
    }

    /***
     * This method to update the book data in realtime database.
     * @param book variable that contain book model
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     * @see Book
     */
    override fun updateBook(book: Book): LiveData<Result<String>> {
        val result = MediatorLiveData<Result<String>>()
        result.value = Result.Loading
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        if (isInternetAvailable()) {
            bookRef.child(book.id!!).updateChildren(book.toMap())
                .addOnSuccessListener {
                    result.value =  Result.Success("Book has been updated.")
                }
                .addOnFailureListener {
                    result.value = Result.Error(it.message.toString())
                    crashlytics.log(it.message.toString())
                }
        } else {
            result.value = Result.Error("Connection Error, Please Check Your Connection.")
        }
        return result
    }

    /***
     * This method to delete book that match the ID from realtime database.
     * @param id variable that indicate book ID
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    override fun deleteBook(id: String): LiveData<Result<String>> {
        val result = MediatorLiveData<Result<String>>()
        result.value = Result.Loading
        val bookRef = db.reference.child(BuildConfig.BOOK_REF)
        if (isInternetAvailable()) {
            bookRef.child(id).removeValue()
                .addOnSuccessListener {
                    result.value = Result.Success("Book has been deleted.")
                }
                .addOnFailureListener {
                    result.value = Result.Error(it.message.toString())
                    crashlytics.log(it.message.toString())
                }
        } else {
            result.value = Result.Error("Connection Error, Please Check Your Connection.")
        }
        return result
    }

    /***
     * This method to check user internet connectivity.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}