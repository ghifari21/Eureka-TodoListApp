package com.gosty.todolistapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gosty.todolistapp.R
import com.gosty.todolistapp.databinding.ActivityDetailBinding
import com.gosty.todolistapp.ui.edit.EditActivity
import com.gosty.todolistapp.utils.Result
import com.gosty.todolistapp.utils.ViewModelFactory
import com.kennyc.view.MultiStateView

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_DATA)

        initView(id)
    }

    private fun initView(id: String?) {
        viewModel.getBookById(id!!).observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.stateView.viewState = MultiStateView.ViewState.LOADING
                }

                is Result.Success -> {
                    binding.apply {
                        stateView.viewState = MultiStateView.ViewState.CONTENT

                        Glide.with(this@DetailActivity)
                            .load(it.data.cover)
                            .placeholder(R.drawable.baseline_image_24)
                            .error(R.drawable.baseline_broken_image_24)
                            .centerCrop()
                            .into(ivCover)

                        tvDataTitle.text = getString(R.string.show_data, it.data.title)
                        tvDataAuthor.text = getString(R.string.show_data, it.data.author)
                        tvDataYear.text = getString(R.string.show_data, it.data.year.toString())
                        tvDataCategory.text = getString(R.string.show_data, it.data.category)

                        btnUpdate.setOnClickListener { view ->
                            val intent = Intent(this@DetailActivity, EditActivity::class.java)
                            intent.putExtra(EditActivity.EXTRA_DATA, it.data)
                            startActivity(intent)
                        }

                        btnDelete.setOnClickListener {
                            viewModel.deleteBook(id)
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

    companion object {
        const val EXTRA_DATA = "extra"
    }
}