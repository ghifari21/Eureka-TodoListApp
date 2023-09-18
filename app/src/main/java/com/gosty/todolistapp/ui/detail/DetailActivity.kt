package com.gosty.todolistapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gosty.todolistapp.R
import com.gosty.todolistapp.databinding.ActivityDetailBinding
import com.gosty.todolistapp.ui.edit.EditActivity
import com.gosty.todolistapp.utils.Result
import com.kennyc.view.MultiStateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_DATA)

        initView(id)
    }

    /***
     * This method to initialize the view for DetailActivity.
     * @param id variable that indicate book ID
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    private fun initView(id: String?) {
        viewModel.getBookById(id!!).observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.stateView.viewState = MultiStateView.ViewState.LOADING
                }

                is Result.Success -> {
                    binding.apply {
                        stateView.viewState = MultiStateView.ViewState.CONTENT

                        if (it.data.cover != null) {
                            Glide.with(this@DetailActivity)
                                .load(it.data.cover)
                                .placeholder(R.drawable.baseline_image_24)
                                .error(R.drawable.baseline_broken_image_24)
                                .centerCrop()
                                .into(ivCover)
                        }

                        tvDataTitle.text = getString(R.string.show_data, it.data.title ?: "Null")
                        tvDataAuthor.text = getString(R.string.show_data, it.data.author ?: "Null")
                        tvDataYear.text = getString(R.string.show_data, it.data.year.toString())
                        tvDataCategory.text =
                            getString(R.string.show_data, it.data.category ?: "Null")

                        btnUpdate.setOnClickListener { _ ->
                            val intent = Intent(this@DetailActivity, EditActivity::class.java)
                            intent.putExtra(EditActivity.EXTRA_DATA, it.data)
                            startActivity(intent)
                        }

                        btnDelete.setOnClickListener {
                            showDialogDelete(id)
                        }
                    }
                }

                is Result.Error -> {
                    binding.stateView.viewState = MultiStateView.ViewState.ERROR
                    val errorView = binding.stateView.getView(MultiStateView.ViewState.ERROR)
                    if (errorView != null) {
                        val btn: Button = errorView.findViewById(R.id.btn_retry)
                        btn.setOnClickListener {
                            finish()
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    /***
     * This method to show a dialog as confirmation for deleting the book.
     * @param id variable that indicate book ID
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    private fun showDialogDelete(id: String) {
        AlertDialog.Builder(this@DetailActivity)
            .setTitle(getString(R.string.delete_book))
            .setMessage(getString(R.string.delete_confirm))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteButtonAction(id)
            }
            .setNegativeButton(getString(R.string.no), null)
            .setCancelable(true)
            .create()
            .show()
    }

    /***
     * This method is a action when user deleting the book.
     * @param id variable that indicate book ID
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    private fun deleteButtonAction(id: String) {
        viewModel.deleteBook(id).observe(this@DetailActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.stateView.viewState = MultiStateView.ViewState.LOADING
                }

                is Result.Success -> {
                    binding.stateView.viewState = MultiStateView.ViewState.CONTENT
                    Toast.makeText(this@DetailActivity, result.data, Toast.LENGTH_SHORT).show()
                    finish()
                }

                is Result.Error -> {
                    binding.stateView.viewState = MultiStateView.ViewState.ERROR
                    Toast.makeText(this@DetailActivity, result.error, Toast.LENGTH_SHORT).show()
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

    companion object {
        const val EXTRA_DATA = "extra"
    }
}