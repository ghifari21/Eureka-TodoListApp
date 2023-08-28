package com.gosty.todolistapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.gosty.todolistapp.R
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.data.ui.BookListAdapter
import com.gosty.todolistapp.databinding.ActivityMainBinding
import com.gosty.todolistapp.ui.add.AddActivity
import com.gosty.todolistapp.ui.detail.DetailActivity
import com.gosty.todolistapp.utils.Result
import com.gosty.todolistapp.utils.Utility
import com.gosty.todolistapp.utils.ViewModelFactory
import com.kennyc.view.MultiStateView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance()
    }
    private val adapter = BookListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        viewModel.getAllBooks().observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.stateView.viewState = MultiStateView.ViewState.LOADING
                }

                is Result.Success -> {
                    if (it.data.isNotEmpty()) {
                        binding.apply {
                            stateView.viewState = MultiStateView.ViewState.CONTENT
                            val content = stateView.getView(MultiStateView.ViewState.CONTENT)
                            if (content != null) {
                                val layoutManager = LinearLayoutManager(this@MainActivity)
                                rvBooks.adapter = adapter
                                rvBooks.layoutManager = layoutManager
                                rvBooks.setHasFixedSize(true)
                            }
                        }
                        adapter.submitList(it.data)

                        adapter.setOnItemClickCallback(
                            object : BookListAdapter.OnItemClickCallback {
                                override fun onItemClicked(book: Book) {
                                    val intent =
                                        Intent(this@MainActivity, DetailActivity::class.java)
                                    intent.putExtra(DetailActivity.EXTRA_DATA, book.id)
                                    startActivity(intent)
                                }
                            }
                        )
                    } else {
                        binding.stateView.viewState = MultiStateView.ViewState.EMPTY
                        val empty = binding.stateView.getView(MultiStateView.ViewState.EMPTY)
                        if (empty != null) {
                            val btn: FloatingActionButton = empty.findViewById(R.id.fab_add)
                            btn.setOnClickListener {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        AddActivity::class.java
                                    )
                                )
                            }
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

        binding.fabAdd.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AddActivity::class.java
                )
            )
        }
    }
}