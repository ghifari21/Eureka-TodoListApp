package com.gosty.todolistapp.ui.edit

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gosty.todolistapp.R
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.databinding.ActivityEditBinding
import com.gosty.todolistapp.utils.Result
import com.kennyc.view.MultiStateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private val viewModel: EditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val book = intent.getParcelableExtra<Book>(EXTRA_DATA)
        initView(book)
    }

    private fun initView(book: Book?) {
        binding.apply {
            edtCover.setText(book?.cover)
            edtTitle.setText(book?.title)
            edtAuthor.setText(book?.author)
            edtYear.setText(book?.year.toString())
            edtCategory.setText(book?.category)

            btnSubmit.setOnClickListener {
                if (validateInput()) {
                    val cover = binding.edtCover.text.toString().trim()
                    val title = binding.edtTitle.text.toString().trim()
                    val author = binding.edtAuthor.text.toString().trim()
                    val year = binding.edtYear.text.toString().trim()
                    val category = binding.edtCategory.text.toString().trim()

                    val newBook = Book(
                        id = book?.id,
                        cover = cover,
                        title = title,
                        author = author,
                        year = year.toInt(),
                        category = category
                    )

                    viewModel.updateBook(newBook)
                } else {
                    Toast.makeText(
                        this@EditActivity,
                        getString(R.string.invalid_input),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.result.observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.stateView.viewState = MultiStateView.ViewState.LOADING
                }

                is Result.Success -> {
                    binding.stateView.viewState = MultiStateView.ViewState.CONTENT
                    Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()
                    finish()
                }

                is Result.Error -> {
                    binding.stateView.viewState = MultiStateView.ViewState.ERROR
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    val errorView = binding.stateView.getView(MultiStateView.ViewState.ERROR)
                    if (errorView != null) {
                        val btn: Button = errorView.findViewById(R.id.btn_retry)
                        btn.text = getString(R.string.reload)
                        btn.setOnClickListener {
                            finish()
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        var result = true
        val cover = binding.edtCover.text.toString().trim()
        val title = binding.edtTitle.text.toString().trim()
        val author = binding.edtAuthor.text.toString().trim()
        val year = binding.edtYear.text.toString().trim()
        val category = binding.edtCategory.text.toString().trim()

        // check empty field
        if (cover.isEmpty()) {
            result = false
            binding.edtCover.error = getString(R.string.empty_field)
        }
        if (title.isEmpty()) {
            result = false
            binding.edtTitle.error = getString(R.string.empty_field)
        }
        if (author.isEmpty()) {
            result = false
            binding.edtAuthor.error = getString(R.string.empty_field)
        }
        if (year.isEmpty()) {
            result = false
            binding.edtYear.error = getString(R.string.empty_field)
        }
        if (category.isEmpty()) {
            result = false
            binding.edtCategory.error = getString(R.string.empty_field)
        }

        return result
    }

    companion object {
        const val EXTRA_DATA = "extra"
    }
}